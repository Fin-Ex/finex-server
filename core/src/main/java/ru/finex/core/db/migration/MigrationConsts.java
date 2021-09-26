package ru.finex.core.db.migration;

import lombok.experimental.UtilityClass;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class MigrationConsts {

    public static final String MIGRATION_TABLE = """
        create table if not exists db_evolutions(
            id serial primary key,
            component varchar not null,
            version int not null,
            checksum varchar not null,
            up_queries json not null,
            down_queries json not null,
            apply_timestamp timestamp default now()
        )
        """;

    public static final String MIGRATION_INDEX = """
        create unique index if not exists db_evolutions_component_version_idx on db_evolutions(component, version)
    """;

}
