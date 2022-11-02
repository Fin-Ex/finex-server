package ru.finex.core.component.prototype;

import lombok.Data;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
public class EquipPrototype implements ComponentPrototype {

    private String type;
    private WeaponType weaponType;
    private int attackPower;

    public enum WeaponType {
        Blunt,
        Sword;
    }

}
