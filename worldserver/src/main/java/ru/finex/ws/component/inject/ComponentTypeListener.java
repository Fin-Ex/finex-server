package ru.finex.ws.component.inject;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.ws.component.InjectComponent;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ComponentTypeListener implements TypeListener {

    private final GameObject gameObject;

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        Provider<ComponentService> componentServiceProvider = typeEncounter.getProvider(ComponentService.class);
        Class<? super I> type = typeLiteral.getRawType();
        FieldUtils.getFieldsListWithAnnotation(type, InjectComponent.class)
            .stream()
            .filter(field -> Component.class.isAssignableFrom(field.getType()))
            .forEach(field -> typeEncounter.register(new ComponentInjector<>(field, componentServiceProvider, gameObject)));
    }

}
