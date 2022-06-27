package ru.finex.core.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.model.entity.EntityObject;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.concurrent.Future;
import javax.persistence.Query;

/**
 * CRUD repository proxy.
 * Implements all base methods from {@link AbstractCrudRepository AbstractCrudRepository} and support
 *  to processing custom methods with {@link ru.finex.core.repository.Query Query} or {@link NamedQuery NamedQuery} annotations.
 *
 * @param <E> entity type
 * @param <ID> entity id type
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class RepositoryProxy<E extends EntityObject<ID>, ID extends Serializable> implements InvocationHandler {

    private final DefaultCrudRepository<E, ID> repository;
    @Getter
    private final Class<? extends CrudRepository<E, ID>> interfaceType;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.setAccessible(true);

        Object result;
        try {
            result = method.invoke(repository, args);
        } catch (IllegalArgumentException e) {
            result = customMethodInvoke(method, args);
        }

        return result;
    }

    private Object customMethodInvoke(Method method, Object[] args) {
        return customMethodInvoke(method, args, method.getReturnType());
    }

    private Object customMethodInvoke(Method method, Object[] args, Class<?> returnType) {
        if (Future.class.isAssignableFrom(returnType)) {
            ParameterizedType typeWithGeneric = (ParameterizedType) method.getGenericReturnType();
            Class<?> generic = (Class<?>) typeWithGeneric.getActualTypeArguments()[0];
            return repository.asyncOperation(() -> customMethodInvoke(method, args, generic));
        } else if (Collection.class.isAssignableFrom(returnType)) {
            return listObject(method, args);
        } else {
            return singleObject(method, args);
        }
    }

    private Object listObject(Method method, Object[] args) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query query = getQuery(method, session);
            passParameters(method, query, args);
            Object result = query.getResultList();
            ctx.commit(session);
            return result;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    private Object singleObject(Method method, Object[] args) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query query = getQuery(method, session);
            passParameters(method, query, args);
            Object result = query.getSingleResult();
            ctx.commit(session);
            return result;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    private Query getQuery(Method method, Session session) {
        var metaQuery = method.getAnnotation(ru.finex.core.repository.Query.class);
        if (metaQuery == null) {
            var metaNamedQuery = method.getAnnotation(NamedQuery.class);
            if (metaNamedQuery == null || StringUtils.isBlank(metaNamedQuery.value())) {
                return session.createNamedQuery(interfaceType.getSimpleName() + "." + method.getName());
            }

            return session.createNamedQuery(metaNamedQuery.value());
        }

        return session.createQuery(metaQuery.value());
    }

    private void passParameters(Method method, Query query, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            var param = parameters[i];

            String name;
            QueryParam metaParam = param.getAnnotation(QueryParam.class);
            if (metaParam == null) {
                name = param.getName();
            } else {
                name = metaParam.value();
            }

            query.setParameter(name, args[i]);
        }
    }

}
