package ru.finex.core.component.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.finex.core.component.AppearanceComponent;
import ru.finex.core.component.ComponentService;
import ru.finex.core.component.ComponentTest;
import ru.finex.core.component.EquipComponent;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.GameObjectModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.object.GameObject;
import ru.finex.testing.container.Container;
import ru.finex.testing.container.ContainerType;
import ru.finex.testing.server.Server;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author m0nster.mind
 */
@Container(ContainerType.Database)
@Server(config = "database-test.conf", modules = {
        HoconModule.class,
        DbModule.class,
        GameObjectModule.class,
        MapperModule.class
})
public class ComponentMapperTest {

    @Inject
    private ComponentService componentService;

    @Test
    public void testGameObjectInject() {
        GameObject gameObject = mock(GameObject.class);
        when(gameObject.getRuntimeId()).thenReturn(1);
        when(gameObject.getPersistenceId()).thenReturn(3); // goblin warrior

        componentService.addComponentsFromPrototype("Goblin Warrior", gameObject);

        ComponentTest.testComponents(gameObject);

        Assertions.assertTrue(componentService.removeComponent(gameObject, AppearanceComponent.class));
        Assertions.assertTrue(componentService.removeComponent(gameObject, EquipComponent.class));
    }

}
