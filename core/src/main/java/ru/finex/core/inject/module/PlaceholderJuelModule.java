package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.placeholder.PlaceholderService;
import ru.finex.core.placeholder.juel.JuelExpressionFactoryProvider;
import ru.finex.core.placeholder.juel.PlaceholderServiceImpl;

import javax.el.ExpressionFactory;

/**
 * @author m0nster.mind
 */
public class PlaceholderJuelModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExpressionFactory.class).toProvider(JuelExpressionFactoryProvider.class);
        bind(PlaceholderService.class).to(PlaceholderServiceImpl.class);
    }

}
