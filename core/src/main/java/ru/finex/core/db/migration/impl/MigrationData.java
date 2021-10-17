package ru.finex.core.db.migration.impl;

import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MigrationData {

    private String component;
    private int version;
    private String name;
    private List<String> upQueries = new ArrayList<>();
    private List<String> downQueries = new ArrayList<>();

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static MigrationData combine(MigrationData o1, MigrationData o2) {
        return new MigrationData(
            o1.getComponent(),
            o1.getVersion(),
            Optional.ofNullable(o1.getName()).orElse("?") + "+" + Optional.ofNullable(o2.getName()).orElse("?"),
            Streams.concat(o1.getUpQueries().stream(), o2.getUpQueries().stream())
                .collect(Collectors.toList()),
            Streams.concat(o1.getDownQueries().stream(), o2.getDownQueries().stream())
                .collect(Collectors.toList())
        );
    }

}
