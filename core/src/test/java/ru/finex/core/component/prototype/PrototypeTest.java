package ru.finex.core.component.prototype;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.finex.core.component.prototype.EquipPrototype.WeaponType;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.GameObjectPrototypeService;
import ru.finex.testing.container.Container;
import ru.finex.testing.container.ContainerType;
import ru.finex.testing.server.Server;

import java.util.List;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Container(ContainerType.Database)
@Server(config = "database-test.conf", modules = {
        HoconModule.class,
        DbModule.class
})
public class PrototypeTest {

    @Inject
    private GameObjectPrototypeService prototypeService;

    @Test
    public void testGoblinWarrior() {
        List<ComponentPrototype> prototypes = prototypeService.getPrototypesByName("Goblin Warrior");
        Assertions.assertEquals(2, prototypes.size());
        for (ComponentPrototype prototype : prototypes) {
            if (prototype instanceof EquipPrototype equipPrototype) {
                Assertions.assertEquals("goblin warrior", equipPrototype.getType());
                Assertions.assertEquals(10, equipPrototype.getAttackPower());
                Assertions.assertEquals(WeaponType.Blunt, equipPrototype.getWeaponType());
            } else if (prototype instanceof AppearancePrototype appearancePrototype) {
                Assertions.assertEquals("appearance", appearancePrototype.getType());
                Assertions.assertEquals("goblin", appearancePrototype.getModel());
            } else {
                Assertions.fail("Unknown prototype: " + prototype.getClass().getCanonicalName() + ": " + prototype);
            }
        }
    }

}
