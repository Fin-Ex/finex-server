package ru.finex.core.cluster.impl;

import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.GlobalContext;
import ru.finex.core.cluster.impl.providers.ClusteredProvider;
import ru.finex.core.utils.ClassUtils;
import ru.finex.core.utils.GenericUtils;

import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClusteredProviders {

    private final Map<Class<?>, ClusteredProvider<?>> providers;

    public ClusteredProviders() {
        providers = GlobalContext.reflections.getSubTypesOf(ClusteredProvider.class)
            .stream()
            .map(clazz -> Pair.of(
                GenericUtils.getInterfaceGenericType(clazz, ClusteredProvider.class, 0),
                (ClusteredProvider<?>) ClassUtils.createInstance(clazz))
            ).collect(Collectors.toMap(
                Pair::getKey,
                Pair::getValue
            ));
    }

    /**
     * Search clustered provider by specified type.
     * @param type type
     * @return clustered provider or null if provider not found
     */
    public ClusteredProvider<?> get(Class<?> type) {
        return providers.get(type);
    }

}
