# See https://github.com/Fin-Ex/finex-evolution
# --- !Ups

create table if not exists theGame_test_table(
    id bigserial primary key,
    data varchar not null
);

# --- !Downs

drop table if exists theGame_test_table;