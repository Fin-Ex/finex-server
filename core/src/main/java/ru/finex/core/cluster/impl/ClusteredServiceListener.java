package ru.finex.core.cluster.impl;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import ru.finex.core.placeholder.PlaceholderService;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
@RequiredArgsConstructor
public class ClusteredServiceListener implements TypeListener {

    private final Provider<PlaceholderService> placeholderServiceProvider;

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

            RRemoteService remoteService = remoteService(redisson, server);
            for (Class remoteInterface : interfaces) {
                remoteService.register(remoteInterface, injectee);
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
            ClusteredService meta = field.getAnnotation(ClusteredService.class);

            encounter.register((MembersInjector<I>) instance -> {
                RedissonClient redisson = clientProvider.get();
                Class<?> fieldType = field.getType();

                RRemoteService remoteService = remoteService(redisson, meta);
                Object service = remoteService.get(fieldType);
                try {
                    FieldUtils.writeField(field, instance, service, true);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private RRemoteService remoteService(RedissonClient redisson, ClusteredService meta) {
        RRemoteService remoteService;
        if (StringUtils.isBlank(meta.value())) {
            remoteService = redisson.getRemoteService();
        } else {
            PlaceholderService placeholderService = placeholderServiceProvider.get();
            String name = placeholderService.evaluate(meta.value(), String.class);
            remoteService = redisson.getRemoteService(name);
        }

        return remoteService;
    }

}
