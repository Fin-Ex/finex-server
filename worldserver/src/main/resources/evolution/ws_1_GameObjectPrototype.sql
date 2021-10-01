# --- !Ups

create table if not exists game_object_prototypes(
    id serial primary key,
    "name" varchar unique not null
);
comment on table game_object_prototypes is 'Game object prototypes';
comment on column game_object_prototypes."name" is 'Name of game object prototype';

create table if not exists game_object_component_prototypes(
    id serial primary key,
    component varchar not null,
    prototype_id int not null references game_object_prototypes(id) on delete cascade on update cascade
);
comment on table game_object_component_prototypes is 'Component list for specified game object prototype';
comment on column game_object_component_prototypes.component is 'Canonical class name to specified component';
comment on column game_object_component_prototypes.prototype_id is 'Reference to prototype';

# --- !Downs

drop table if exists game_object_component_prototypes;
drop table if exists game_object_prototypes;