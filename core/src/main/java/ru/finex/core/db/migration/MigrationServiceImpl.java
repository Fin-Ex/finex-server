package ru.finex.core.db.migration;

import com.google.common.collect.ListMultimap;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.service.MigrationService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class MigrationServiceImpl implements MigrationService {

    private final ListMultimap<String, MigrationData> migrations;
    private final MigrationDao migrationDao;
    private final MigrationTree migrationTree;

    @Inject
    public MigrationServiceImpl(MigrationParser parser, MigrationDao migrationDao, MigrationTree migrationTree) {
        migrations = parser.parseAll();
        joinMigrations();

        this.migrationDao = migrationDao;
        this.migrationTree = migrationTree;
    }

    private void joinMigrations() {
        List<String> components = new ArrayList<>(migrations.keySet());
        for (String component : components) {
            List<MigrationData> migrations = this.migrations.get(component);
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

                if (!checksum.equals(appliedChecksum)) {
                    log.warn("Detect changed evolution! Rollback exists migration '{}', version: {}", component, data.getVersion());
                    migrationDao.rollbackAndDeleteRecursive(component, data.getVersion());
                    isRollback = true;
                }
            }

            migrationDao.applyAndSave(data, checksum);
        }

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
