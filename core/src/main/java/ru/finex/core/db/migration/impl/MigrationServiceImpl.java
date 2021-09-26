package ru.finex.core.db.migration.impl;

import com.google.common.collect.ListMultimap;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.migration.MigrationService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class MigrationServiceImpl implements MigrationService {

    private static final String AUTO_ROLLBACK_FLAG = "--evolution-auto-rollback";

    private final ListMultimap<String, MigrationData> migrations;
    private final MigrationDao migrationDao;
    private final MigrationTree migrationTree;

    @Inject
    public MigrationServiceImpl(MigrationParser parser, MigrationDao migrationDao, MigrationTree migrationTree) {
        migrations = handleMigrations(parser);
        this.migrationDao = migrationDao;
        this.migrationTree = migrationTree;

        migrationDao.install();
    }

    private ListMultimap<String, MigrationData> handleMigrations(MigrationParser parser) {
        ListMultimap<String, MigrationData> migrationMultimap = parser.parseAll();

        List<String> components = new ArrayList<>(migrationMultimap.keySet());
        for (String component : components) {
            List<MigrationData> migrations = migrationMultimap.get(component);
            List<MigrationData> joined = migrations.stream()
                .mapToInt(MigrationData::getVersion)
                .distinct()
                .mapToObj(version -> migrations.stream()
                    .filter(data -> data.getVersion() == version)
                    .reduce(MigrationData::combine)
                    .orElse(null)
                ).filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(MigrationData::getVersion))
                .collect(Collectors.toList());
            migrations.clear();
            migrations.addAll(joined);
        }

        return migrationMultimap;
    }

    @Override
    public void autoMigration() {
        migrationTree.applyOperation(this::migrate);
    }

    @Override
    public void migrate(String component) {
        MessageDigest digest = createDigest();
        List<MigrationData> migrations = this.migrations.get(component);
        List<String> appliedChecksums = migrationDao.getChecksumsByComponent(component);

        boolean isRollback = false;
        for (int i = 0; i < migrations.size(); i++) {
            MigrationData data = migrations.get(i);
            String checksum = calculateChecksum(digest, data);

            if (i < appliedChecksums.size() && !isRollback) {
                String appliedChecksum = appliedChecksums.get(i);

                if (checksum.equals(appliedChecksum)) {
                    continue; // already applied
                }

                log.error("Detect evolution changes: '{}' version {}. Current checksum '{}', new checksum '{}'",
                    component, data.getVersion(), appliedChecksum, checksum);

                if (GlobalContext.arguments.containsKey(AUTO_ROLLBACK_FLAG)) {
                    log.warn("Rollback evolutions {}+ for '{}'.", data.getVersion(), component);
                    migrationDao.rollbackAndDeleteRecursive(component, data.getVersion());
                } else {
                    throw new RuntimeException(String.format(
                        "Evolution %s has changes! To enable auto rollback and deploy use '%s'",
                        Optional.ofNullable(data.getName())
                            .orElseGet(() -> getEvolutionFilename(data)),
                        AUTO_ROLLBACK_FLAG
                    ));
                }
                isRollback = true;
            }

            migrationDao.applyAndSave(data, checksum);
        }

    }

    private String getEvolutionFilename(MigrationData data) {
        return data.getComponent() + "_" + data.getVersion();
    }

    private String calculateChecksum(MessageDigest digest, MigrationData data) {
        digest.reset();
        data.getUpQueries().forEach(query -> digest.update(query.getBytes()));
        data.getDownQueries().forEach(query -> digest.update(query.getBytes()));
        return new String(Base64.getEncoder().encode(digest.digest()));
    }

    private static MessageDigest createDigest() {
        try {
            return MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
