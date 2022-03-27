package ru.finex.core.placeholder.juel;

import lombok.RequiredArgsConstructor;
import ru.finex.core.placeholder.PlaceholderService;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlaceholderServiceImpl implements PlaceholderService {

    private final ExpressionFactory expressionFactory;
    private final ELContext context;

    @Override
    public <T> T evaluate(String expression, Class<T> resultType) {
        ValueExpression valueExpression = expressionFactory.createValueExpression(context, expression, resultType);
        return (T) valueExpression.getValue(context);
    }

}
