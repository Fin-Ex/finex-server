package ru.finex.core.placeholder.juel;

import com.typesafe.config.Config;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.finex.core.placeholder.PlaceholderService;

import java.lang.reflect.Field;
import javax.el.ArrayELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ValueExpression;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlaceholderServiceImpl implements PlaceholderService {

    private final ExpressionFactory expressionFactory;
    private final Config config;

    @Override
    public <T> T evaluate(String expression, Object caller, Class<T> resultType) {
        ELContext context = context(caller);
        ValueExpression valueExpression = expressionFactory.createValueExpression(context, expression, resultType);
        return (T) valueExpression.getValue(context);
    }

    private ELContext context(Object caller) {
        SimpleContext context = new SimpleContext();
        context.setELResolver(createResolver());

        context.setVariable("config", expressionFactory.createValueExpression(config, Config.class));
        if (caller != null) {
            attachCallerContext(context, caller);
        }
        return context;
    }

    private ELResolver createResolver() {
        ELResolver resolver = new CompositeELResolver() {
            {
                add(new ArrayELResolver(true));
                add(new ListELResolver(true));
                add(new MapELResolver(true));
                add(new ELConfigResolver());
            }
        };

        return new SimpleResolver(resolver, true);
    }

    private void attachCallerContext(SimpleContext context, Object caller) {
        for (Field field : FieldUtils.getAllFields(caller.getClass())) {
            Object value;
            try {
                value = FieldUtils.readField(field, caller, true);
            } catch (ReflectiveOperationException e) {
                log.error("Fail to read field {}::{} to put it into EL context",
                    caller.getClass().getCanonicalName(),
                    field.getName()
                );
                continue;
            }
            context.setVariable(
                field.getName(),
                expressionFactory.createValueExpression(value, field.getType())
            );
        }
    }

}
