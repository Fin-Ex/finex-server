# --- !Ups

create table if not exists game_object_prototypes(
    id serial primary key,
    "name" varchar unique not null,
    parent_id int references game_object_prototypes(id)
);
comment on table game_object_prototypes is 'Game object prototypes';
comment on column game_object_prototypes."name" is 'Name of game object prototype';
comment on column game_object_prototypes.parent_id is 'Parent component collection (reference to other prototype)';

create table if not exists game_object_component_prototypes(
    id serial primary key,
    component varchar not null,
    prototype_id int not null references game_object_prototypes(id) on delete cascade on update cascade,
    data jsonb not null,
    parent_id int references game_object_component_prototypes(id)
);
comment on table game_object_component_prototypes is 'Component list for specified game object prototype';
comment on column game_object_component_prototypes.component is 'Canonical class name to specified component';
comment on column game_object_component_prototypes.prototype_id is 'Reference to prototype';
comment on column game_object_component_prototypes.data is 'JSON data of prototype';
comment on column game_object_component_prototypes.parent_id is 'Parent component data (reference to other component prototype)';

create aggregate jsonb_merge_object(jsonb) (SFUNC = 'jsonb_concat', STYPE = jsonb, INITCOND = '{}');

# --- !Downs

drop aggregate if exists jsonb_merge_object(jsonb);

drop table if exists game_object_component_prototypes;
drop table if exists game_object_prototypes;