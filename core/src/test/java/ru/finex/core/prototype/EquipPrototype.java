package ru.finex.core.prototype;

import lombok.Data;

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
