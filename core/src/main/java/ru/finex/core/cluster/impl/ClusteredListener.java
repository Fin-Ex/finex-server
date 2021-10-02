package ru.finex.core.cluster.impl;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.impl.providers.ClusteredProvider;
import ru.finex.core.utils.ParameterUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ClusteredListener implements TypeListener {

    private final Provider<ClusteredProviders> clusteredProvidersProvider;

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

            String name = getName(clazz, field);

            encounter.register((MembersInjector<I>) instance -> {
                Object value = provideObject(clientProvider, clazz, field, name);
                try {
                    FieldUtils.writeField(field, instance, value, true);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
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
        Parameter[] parameters = method.getParameters();
        Object[] parameterValues = new Object[parameters.length];
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
            }

            parameterValues[i] = value;
        }

        try {
            method.invoke(instance, parameterValues);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private String getName(Class<?> type, Field field) {
        Clustered clustered = field.getAnnotation(Clustered.class);
        String name = clustered.value();
        if (StringUtils.isBlank(name)) {
            name = type.getCanonicalName() + "#" + field.getName();
        }

        return name;
    }

    private String getName(Class<?> type, String methodName, String parameterName, Clustered clustered) {
        String name = clustered.value();
        if (StringUtils.isBlank(name)) {
            name = type.getCanonicalName() + "::" + methodName + "#" + parameterName;
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
