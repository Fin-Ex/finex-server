package ru.finex.core.cluster.impl;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RObject;
import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.impl.providers.ClusteredProvider;
import ru.finex.core.utils.ParameterUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ClusteredListener implements TypeListener {

    private final Provider<ClusteredProviders> clusteredProvidersProvider;
    private final Provider<ClusterService> clusterServiceProvider;

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<?> clazz = type.getRawType();
        registerFields(clazz, encounter);
        registerMethods(clazz, encounter);
    }

    private <I> void registerFields(Class<?> clazz, TypeEncounter<I> encounter) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, Clustered.class);
        if (fields.isEmpty()) {
            return;
        }

        Provider<RedissonClient> clientProvider = encounter.getProvider(RedissonClient.class);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);

            encounter.register((MembersInjector<I>) instance -> {
                var meta = getAnnotationInfo(clazz, field);
                Object value = provideObject(clientProvider, clazz, field, meta.getLeft());
                try {
                    FieldUtils.writeField(field, instance, value, true);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }

                if (meta.getRight() && value instanceof RObject resource) {
                    ClusterService clusterService = clusterServiceProvider.get();
                    clusterService.registerManagedResource(resource);
                }
            });
        }
    }

    private <I> void registerMethods(Class<?> clazz, TypeEncounter<I> encounter) {
        List<Method> methods = ParameterUtils.getMethodsWithParameterAnnotation(clazz, Clustered.class, true, true);
        if (methods.isEmpty()) {
            return;
        }

        Provider<RedissonClient> clientProvider = encounter.getProvider(RedissonClient.class);
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);

            encounter.register((MembersInjector<I>) instance -> injectMethod(clientProvider, encounter, instance, clazz, method));
        }
    }

    private void injectMethod(Provider<RedissonClient> clientProvider, TypeEncounter<?> encounter, Object instance, Class<?> clazz, Method method) {
        ClusterService clusterService = clusterServiceProvider.get();
        Parameter[] parameters = method.getParameters();
        Object[] parameterValues = new Object[parameters.length];
        List<RObject> managedResources = new ArrayList<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            Class<?> type = parameter.getType();
            Clustered clustered = parameter.getAnnotation(Clustered.class);

            Object value;
            if (clustered == null) {
                value = encounter.getProvider(type).get();
            } else {
                String methodName = method.getName();
                String parameterName = parameter.getName();
                String name = getName(clazz, methodName, parameterName, clustered);
                value = provideObject(clientProvider, clazz, type, methodName, parameter, name);

                if (clustered.autoManagement() && value instanceof RObject resource) {
                    managedResources.add(resource);
                }
            }

            parameterValues[i] = value;
        }

        try {
            method.invoke(instance, parameterValues);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        managedResources.forEach(clusterService::registerManagedResource);
    }

    private Pair<String, Boolean> getAnnotationInfo(Class<?> type, Field field) {
        ClusterService clusterService = clusterServiceProvider.get();
        Clustered clustered = field.getAnnotation(Clustered.class);
        String name = clustered.value();
        if (StringUtils.isBlank(name)) {
            name = clusterService.getName(type, field.getName());
        }

        return Pair.of(name, clustered.autoManagement());
    }

    private String getName(Class<?> type, String methodName, String parameterName, Clustered clustered) {
        ClusterService clusterService = clusterServiceProvider.get();
        String name = clustered.value();
        if (StringUtils.isBlank(name)) {
            name = clusterService.getName(type, methodName, parameterName);
        }

        return name;
    }

    private Object provideObject(Provider<RedissonClient> clientProvider, Class<?> clazz, Field field, String name) {
        RedissonClient client = clientProvider.get();
        Class<?> type = field.getType();

        ClusteredProvider<?> clusteredProvider = clusteredProvidersProvider.get()
            .get(type);

        Objects.requireNonNull(clusteredProvider, String.format(
            "Clustered provider not found for type %s (location: %s#%s)",
            type.getCanonicalName(),
            clazz.getCanonicalName(),
            field.getName()
        ));

        Object object = clusteredProvider.get(field.getGenericType(), client, name);

        Objects.requireNonNull(object, String.format(
            "Unknown field type: %s::%s to inject with @%s",
            clazz.getCanonicalName(),
            field.getName(),
            Clustered.class.getName()
        ));

        return object;
    }

    private Object provideObject(Provider<RedissonClient> clientProvider, Class<?> clazz, Class<?> type,
        String methodName, Parameter parameter, String name) {
        RedissonClient client = clientProvider.get();

        ClusteredProvider<?> clusteredProvider = clusteredProvidersProvider.get()
            .get(type);

        Objects.requireNonNull(clusteredProvider, String.format(
            "Clustered provider not found for type %s (location: %s::%s#%s)",
            type.getCanonicalName(),
            clazz.getCanonicalName(),
            methodName,
            parameter.getName()
        ));

        Object object = clusteredProvider.get(parameter.getParameterizedType(), client, name);

        Objects.requireNonNull(object, String.format(
            "Unknown method parameter type: %s::%s#%s to inject with @%s",
            clazz.getCanonicalName(),
            methodName,
            parameter.getName(),
            Clustered.class.getName()
        ));

        return object;
    }

}
