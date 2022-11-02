package ru.finex.core.component.mapper;

import ru.finex.core.component.EquipComponent;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.component.prototype.EquipPrototype;

/**
 * @author m0nster.mind
 */
public class EquipComponentMapper implements ComponentPrototypeMapper<EquipPrototype, EquipComponent> {

    @Override
    public EquipComponent map(EquipPrototype prototype) {
        EquipComponent component = new EquipComponent();
        component.setAttackPower(prototype.getAttackPower());
        component.setWeaponType(prototype.getWeaponType());
        return component;
    }

}
