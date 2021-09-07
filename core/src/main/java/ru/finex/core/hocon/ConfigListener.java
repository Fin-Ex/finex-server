package ru.finex.core.hocon;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author m0nster.mind
 */
public class ConfigListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<? super I> clazz = type.getRawType();

        Map<Field, InjectionContext> injectionContexts = new HashMap<>();

        InjectionContext classContext = createClassContext(clazz);
        Field[] fields;
        if (clazz.isAnnotationPresent(ConfigResource.class)) {
            fields = FieldUtils.getAllFields(clazz);
        } else {
            fields = FieldUtils.getFieldsWithAnnotation(clazz, ConfigResource.class);
        }

        for (Field field : fields) {
            InjectionContext ctx = createFieldContext(classContext, clazz, field);
            if (ctx != null) {
                injectionContexts.put(field, ctx);
            }
        }

        Provider<Config> configProvider = encounter.getProvider(Config.class);
        injectionContexts.entrySet()
            .stream()
            .map(entry -> new ConfigInjectionListener<I>(
                entry.getKey(), configProvider,
                entry.getValue().getBasePath() + "." + entry.getValue().getProperty(),
                entry.getValue().isCanMissing(),
                entry.getValue().isNullable()
            )).forEach(encounter::register);
    }

    private InjectionContext createFieldContext(InjectionContext classContext, Class<?> clazz, Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
            return null;
        }
        field.setAccessible(true);

        ConfigResource configResource = field.getAnnotation(ConfigResource.class);
        InjectionContext ctx = extendContext(classContext, configResource);
        ctx.replaceExpressions(clazz, field);
        return ctx;
    }

    private InjectionContext createClassContext(Class<?> clazz) {
        InjectionContext ctx = new InjectionContext();

        ConfigResource configResource = clazz.getAnnotation(ConfigResource.class);
        if (configResource == null) {
            return ctx;
        }

        if (!StringUtils.isBlank(configResource.value())) {
            ctx.setProperty(configResource.value());
        }

        if (!StringUtils.isBlank(configResource.basePath())) {
            ctx.setBasePath(configResource.basePath());
        }

        ctx.setCanMissing(configResource.canMissing());
        ctx.setNullable(configResource.nullable());
        return ctx;
    }

    private InjectionContext extendContext(InjectionContext ctx, ConfigResource configResource) {
        InjectionContext subContext = new InjectionContext();

        if (configResource == null || StringUtils.isBlank(configResource.value())) {
            subContext.setProperty(ctx.getProperty());
        } else {
            subContext.setProperty(configResource.value());
        }

        if (configResource == null || StringUtils.isBlank(configResource.basePath())) {
            subContext.setBasePath(ctx.getBasePath());
        } else {
            subContext.setBasePath(configResource.basePath());
        }

        if (configResource == null) {
            subContext.setCanMissing(ctx.isCanMissing());
        } else {
            subContext.setCanMissing(configResource.canMissing());
        }

        if (configResource == null) {
            subContext.setNullable(ctx.isNullable());
        } else {
            subContext.setNullable(configResource.nullable());
        }

        return subContext;
    }

}
