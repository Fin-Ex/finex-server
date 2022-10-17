package ru.finex.core.component;

import lombok.Data;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectScoped;
import ru.finex.core.prototype.EquipPrototype;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Data
@GameObjectScoped
public class EquipComponent implements Component {

    @Inject
    private GameObject gameObject;

    private EquipPrototype.WeaponType weaponType;
    private int attackPower;

}
