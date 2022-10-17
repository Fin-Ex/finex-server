package ru.finex.core.component.mapper;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.finex.core.ContainerRule;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerRule;
import ru.finex.core.component.AppearanceComponent;
import ru.finex.core.component.ComponentService;
import ru.finex.core.component.ComponentTest;
import ru.finex.core.component.EquipComponent;
import ru.finex.core.object.GameObject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author m0nster.mind
 */
public class ComponentMapperTest {

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
        GameObject gameObject = mock(GameObject.class);
        when(gameObject.getRuntimeId()).thenReturn(1);
        when(gameObject.getPersistenceId()).thenReturn(3); // goblin warrior

        var componentService = GlobalContext.injector.getInstance(ComponentService.class);
        componentService.addComponentsFromPrototype("Goblin Warrior", gameObject);

        ComponentTest.testComponents(gameObject);

        Assertions.assertTrue(componentService.removeComponent(gameObject, AppearanceComponent.class));
        Assertions.assertTrue(componentService.removeComponent(gameObject, EquipComponent.class));
    }

}
