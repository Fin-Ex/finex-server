package ru.finex.core.cluster.impl;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
public class ClusteredServiceListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<?> clazz = type.getRawType();

        registerServer(encounter, clazz);
        registerFields(encounter, clazz);
    }

    private <I> void registerServer(TypeEncounter<I> encounter, Class<?> type) {
        ClusteredService server = type.getAnnotation(ClusteredService.class);
        if (server == null) {
            return;
        }

        Class[] interfaces = type.getInterfaces();
        if (interfaces.isEmpty()) {
            log.warn("Class '{}' is marked as ClusterService but didnt implement any interface!", type.getCanonicalName());
            return;
        }

        Provider<RedissonClient> clientProvider = encounter.getProvider(RedissonClient.class);

        encounter.register((InjectionListener<I>) injectee -> {
            RedissonClient redisson = clientProvider.get();

            for (Class remoteInterface : interfaces) {
                redisson.getRemoteService()
                    .register(remoteInterface, injectee);
            }
        });
    }

    private <I> void registerFields(TypeEncounter<I> encounter, Class<?> type) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(type, ClusteredService.class);
        if (fields.isEmpty()) {
            return;
        }

        Provider<RedissonClient> clientProvider = encounter.getProvider(RedissonClient.class);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);

            encounter.register((MembersInjector<I>) instance -> {
                RedissonClient redisson = clientProvider.get();
                Class<?> fieldType = field.getType();

                Object service = redisson.getRemoteService().get(fieldType);
                try {
                    FieldUtils.writeField(field, instance, service, true);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
