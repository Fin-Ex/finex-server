# --- !Ups

create table if not exists game_object_templates(
    id serial primary key,
    "name" varchar unique not null
);
comment on table game_object_templates is 'Game object templates';
comment on column game_object_templates."name" is 'Name of game object template';

create table if not exists game_object_component_templates(
    id serial primary key,
    component varchar not null,
    game_object_template_id int not null references game_object_templates(id)
);
comment on table game_object_component_templates is 'Component list for specified game object template';
comment on column game_object_component_templates.component is 'Canonical class name to specified component';
comment on column game_object_component_templates.game_object_template_id is 'Reference to template';

# --- !Downs

drop table if exists game_object_component_templates;
drop table if exists game_object_templates;