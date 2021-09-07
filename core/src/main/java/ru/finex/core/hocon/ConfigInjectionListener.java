package ru.finex.core.hocon;

import com.google.inject.spi.InjectionListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import lombok.RequiredArgsConstructor;
import ru.finex.core.utils.ClassUtils;

import java.lang.reflect.Field;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ConfigInjectionListener<I> implements InjectionListener<I> {

    private final Field field;
    private final Provider<Config> configProvider;
    private final String path;
    private final boolean acceptMissing;
    private final boolean acceptNullable;

    @Override
    public void afterInjection(I injectee) {
        Config config = configProvider.get();
        Class<?> fieldType = field.getType();

        if (!config.hasPath(path)) {
            if (acceptMissing) {
                return;
            }

            throw new RuntimeException("Missing config path: '" + path + "', required in: " +
                ClassUtils.toStringClassAndField(injectee.getClass(), field));
        }

        if (config.getIsNull(path)) {
            if (acceptNullable) {
                return;
            }

            throw new RuntimeException("Null value in config path: '" + path + "', required in: " +
                ClassUtils.toStringClassAndField(injectee.getClass(), field));
        }

        try {
            if (fieldType.isPrimitive()) {
                injectPrimitive(injectee, fieldType, config);
            } else if (String.class.isAssignableFrom(fieldType)) {
                field.set(injectee, config.getString(path));
            } else {
                field.set(injectee, ConfigBeanFactory.create(config.getConfig(path), fieldType));
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Fail to inject config-value: '" + path + "' for '" +
                ClassUtils.toStringClassAndField(injectee.getClass(), field) + "' member.");
        }
    }

    private void injectPrimitive(I injectee, Class<?> fieldType, Config config) throws ReflectiveOperationException {
        if (byte.class.isAssignableFrom(fieldType)) {
            field.setByte(injectee, (byte) config.getInt(path));
        } else if (short.class.isAssignableFrom(fieldType)) {
            field.setShort(injectee, (short) config.getInt(path));
        } else if (int.class.isAssignableFrom(fieldType)) {
            field.setInt(injectee, config.getInt(path));
        } else if (long.class.isAssignableFrom(fieldType)) {
            field.setLong(injectee, config.getLong(path));
        } else {
            throw new RuntimeException("Unsupported config-type: " + fieldType.getCanonicalName() + " " + path);
        }
    }

}
