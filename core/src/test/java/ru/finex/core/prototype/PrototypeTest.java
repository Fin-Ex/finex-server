package ru.finex.core.prototype;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.finex.core.ContainerRule;
import ru.finex.core.ContainerRule.Type;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerRule;
import ru.finex.core.prototype.EquipPrototype.WeaponType;

import java.util.List;

/**
 * @author m0nster.mind
 */
public class PrototypeTest {

    @ClassRule(order = 0)
    public static ContainerRule containers = new ContainerRule(Type.Database);

    @ClassRule(order = 1)
    public static ServerRule server = ServerRule.builder()
        .configPath("database-test.conf")
        .configModule()
        .databaseModule()
        .build();

    @Test
    public void testGoblinWarrior() {
        GameObjectPrototypeService prototypeService = GlobalContext.injector.getInstance(GameObjectPrototypeService.class);
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
