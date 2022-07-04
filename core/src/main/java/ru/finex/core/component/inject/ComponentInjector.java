package ru.finex.core.component.inject;

import com.google.inject.MembersInjector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.object.GameObject;

import java.lang.reflect.Field;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:JavadocType")
public class ComponentInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final Provider<ComponentService> componentServiceProvider;
    private final GameObject gameObject;

    @SneakyThrows
    @Override
    public void injectMembers(T instance) {
        ComponentService componentService = componentServiceProvider.get();
        Component component = componentService.getComponent(gameObject, (Class<? extends Component>) field.getType());
        FieldUtils.writeField(field, instance, component, true);
    }

}
