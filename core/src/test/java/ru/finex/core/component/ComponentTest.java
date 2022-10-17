package ru.finex.core.component;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.finex.core.ContainerRule;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerRule;
import ru.finex.core.component.mapper.MapperModule;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectFactory;
import ru.finex.core.prototype.AppearancePrototype;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.EquipPrototype;
import ru.finex.core.prototype.GameObjectPrototypeService;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author m0nster.mind
 */
public class ComponentTest {

    @ClassRule(order = 0)
    public static ContainerRule containers = new ContainerRule(ContainerRule.Type.Database);

    @ClassRule(order = 1)
    public static ServerRule server = ServerRule.builder()
            .configPath("database-test.conf")
            .configModule()
            .databaseModule()
            .gameObjectModule()
            .module(ServerRule.Module.of(MapperModule.class))
            .build();

    @Test
    public void testGameObjectInject() {
        var gameObjectFactory = GlobalContext.injector.getInstance(GameObjectFactory.class);
        GameObject gameObject = gameObjectFactory.createGameObject("Goblin Warrior", 3);
        Assertions.assertNotNull(gameObject);

        var componentService = GlobalContext.injector.getInstance(ComponentService.class);
        var components = componentService.getComponents(gameObject);
        Assertions.assertEquals(2, components.size());

        testComponents(gameObject);

        var referencedComponent = componentService.addComponent(gameObject, ReferencedComponent.class);
        Assertions.assertEquals(gameObject, referencedComponent.getGameObject());
        Assertions.assertEquals(componentService.getComponent(gameObject, AppearanceComponent.class), referencedComponent.getAppearanceComponent());
        Assertions.assertEquals(componentService.getComponent(gameObject, EquipComponent.class), referencedComponent.getEquipComponent());

        var namedComponent = componentService.addComponent(gameObject, NamedComponent.class);
        Assertions.assertEquals(gameObject, namedComponent.getGameObject());
        Assertions.assertEquals(gameObject.getRuntimeId(), namedComponent.getRuntimeId());
        Assertions.assertEquals(gameObject.getPersistenceId(), namedComponent.getPersistenceId());
    }

    public static void testComponents(GameObject gameObject) {
        var componentService = GlobalContext.injector.getInstance(ComponentService.class);
        var prototypeService = GlobalContext.injector.getInstance(GameObjectPrototypeService.class);
        var prototypes = prototypeService.getPrototypesByName("Goblin Warrior")
                .stream()
                .collect(Collectors.toMap(
                        ComponentPrototype::getClass,
                        Function.identity()
                ));

        var appearancePrototype = (AppearancePrototype) prototypes.get(AppearancePrototype.class);
        Assertions.assertNotNull(appearancePrototype);

        var appearanceComponent = componentService.getComponent(gameObject, AppearanceComponent.class);
        Assertions.assertNotNull(appearanceComponent);
        Assertions.assertEquals(appearancePrototype.getModel(), appearanceComponent.getModel());

        var equipPrototype = (EquipPrototype) prototypes.get(EquipPrototype.class);
        Assertions.assertNotNull(equipPrototype);

        var equipComponent = componentService.getComponent(gameObject, EquipComponent.class);
        Assertions.assertNotNull(equipComponent);
        Assertions.assertEquals(equipPrototype.getAttackPower(), equipComponent.getAttackPower());
        Assertions.assertEquals(equipPrototype.getWeaponType(), equipComponent.getWeaponType());
    }

}
