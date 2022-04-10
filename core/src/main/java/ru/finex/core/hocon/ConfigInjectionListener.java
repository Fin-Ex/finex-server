package ru.finex.core.hocon;

import com.google.inject.spi.InjectionListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigException;
import lombok.RequiredArgsConstructor;
import ru.finex.core.utils.ClassUtils;

import java.lang.reflect.Field;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:JavadocType")
@RequiredArgsConstructor
public class ConfigInjectionListener<I> implements InjectionListener<I> {

    private final Field field;
    private final Provider<Config> configProvider;
    private final String path;
    private final boolean acceptMissing;
    private final boolean acceptNullable;

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public void afterInjection(I injectee) {
        Config config = configProvider.get();
        Class<?> fieldType = field.getType();

        boolean isNull;
        try {
            isNull = config.getIsNull(path);
        } catch (ConfigException.Missing e) {
            if (acceptMissing) {
                return;
            }

            throw new RuntimeException(String.format(
                "Missing config path: '%s', required in: %s",
                path,
                ClassUtils.toStringClassAndField(injectee.getClass(), field)
            ));
        }

        if (isNull) {
            if (acceptNullable) {
                return;
            }

            throw new RuntimeException(String.format(
                "Null value in config path: '%s', required in: %s",
                path,
                ClassUtils.toStringClassAndField(injectee.getClass(), field)
            ));
        }

        try {
            if (fieldType.isPrimitive()) {
                injectPrimitive(injectee, fieldType, config, path);
            } else if (String.class.isAssignableFrom(fieldType)) {
                field.set(injectee, config.getString(path));
            } else {
                field.set(injectee, ConfigBeanFactory.create(config.getConfig(path), fieldType));
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(String.format(
                "Fail to inject config-value: '%s' for '%s' member",
                path,
                ClassUtils.toStringClassAndField(injectee.getClass(), field)
            ));
        }
    }

    private void injectPrimitive(I injectee, Class<?> fieldType, Config config, String path) throws ReflectiveOperationException {
        if (byte.class.isAssignableFrom(fieldType)) {
            field.setByte(injectee, (byte) config.getInt(path));
        } else if (short.class.isAssignableFrom(fieldType)) {
            field.setShort(injectee, (short) config.getInt(path));
        } else if (int.class.isAssignableFrom(fieldType)) {
            field.setInt(injectee, config.getInt(path));
        } else if (long.class.isAssignableFrom(fieldType)) {
            field.setLong(injectee, config.getLong(path));
        } else if (float.class.isAssignableFrom(fieldType)) {
            field.setFloat(injectee, (float) config.getDouble(path));
        } else if (double.class.isAssignableFrom(fieldType)) {
            field.setDouble(injectee, config.getDouble(path));
        } else if (boolean.class.isAssignableFrom(fieldType)) {
            field.setBoolean(injectee, config.getBoolean(path));
        } else {
            throw new RuntimeException(String.format(
                "Unsupported config-type: %s %s",
                fieldType.getCanonicalName(),
                path
            ));
        }
    }

}
