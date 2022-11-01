# --- !Ups

insert into game_object_prototypes values (1, 'Abstract Warrior', null);
insert into game_object_prototypes values (2, 'Goblin', 1);
insert into game_object_prototypes values (3, 'Goblin Warrior', 2);

insert into game_object_component_prototypes values (1, 'ru.finex.core.component.prototype.EquipPrototype', 1, '{"type": "warrior", "weaponType": "Sword"}', null);
insert into game_object_component_prototypes values (2, 'ru.finex.core.component.prototype.EquipPrototype', 2, '{"type": "goblin", "attackPower": "10"}', 1);
insert into game_object_component_prototypes values (3, 'ru.finex.core.component.prototype.AppearancePrototype', 2, '{"type": "appearance", "model": "goblin"}', null);
insert into game_object_component_prototypes values (4, 'ru.finex.core.component.prototype.EquipPrototype', 3, '{"type": "goblin warrior", "weaponType": "Blunt"}', 2);

# --- !Downs
