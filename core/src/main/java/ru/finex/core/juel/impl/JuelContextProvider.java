package ru.finex.core.juel.impl;

import com.typesafe.config.Config;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;
import lombok.RequiredArgsConstructor;

import javax.el.ArrayELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class JuelContextProvider implements Provider<ELContext> {

    private final ExpressionFactory expressionFactory;
    private final Config config;

    @Override
    public ELContext get() {
        SimpleContext context = new SimpleContext();
        context.setELResolver(createResolver());

        context.setVariable("config", expressionFactory.createValueExpression(config, Config.class));
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

}
