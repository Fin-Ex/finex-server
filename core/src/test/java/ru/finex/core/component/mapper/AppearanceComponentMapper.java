package ru.finex.core.component.mapper;

import ru.finex.core.component.AppearanceComponent;
import ru.finex.core.prototype.AppearancePrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;

/**
 * @author m0nster.mind
 */
public class AppearanceComponentMapper implements ComponentPrototypeMapper<AppearancePrototype, AppearanceComponent> {

    @Override
    public AppearanceComponent map(AppearancePrototype prototype) {
        AppearanceComponent component = new AppearanceComponent();
        component.setModel(prototype.getModel());
        return component;
    }

}
