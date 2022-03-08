package ru.finex.core.juel.impl;

import de.odysseus.el.ExpressionFactoryImpl;

import javax.el.ExpressionFactory;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class JuelExpressionFactoryProvider implements Provider<ExpressionFactory> {

    @Override
    public ExpressionFactory get() {
        return new ExpressionFactoryImpl();
    }

}
