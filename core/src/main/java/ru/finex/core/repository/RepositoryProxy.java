package ru.finex.core.repository;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.core.model.entity.Projection;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
        return customMethodInvoke(method, args, new ReturnType(method.getReturnType()));
    }

    private Object customMethodInvoke(Method method, Object[] args, ReturnType returnType) {
        if (returnType.isFuture()) {
            return repository.asyncOperation(() -> customMethodInvoke(method, args, new ReturnType(method)));
        } else if (returnType.isCollection()) {
            return listObject(method, args, new ReturnType(method));
        } else {
            boolean isOptional = returnType.isOptional();
            ReturnType extract;
            if (isOptional) {
                extract = new ReturnType(method);
            } else {
                extract = returnType;
            }

            Object result;
            try {
                result = singleObject(method, args, extract, !isOptional);
            } catch (NoResultException e) {
                if (!isOptional) {
                    throw e;
                }

                result = null;
            }

            if (isOptional) {
                result = Optional.ofNullable(result);
            }

            return result;
        }
    }

    private Object listObject(Method method, Object[] args, ReturnType returnType) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query query = getQuery(method, session, returnType);
            passParameters(method, query, args);
            Object result = getResultList(query, returnType);
            ctx.commit();
            return result;
        } catch (NoResultException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            ctx.rollback();
            throw new RuntimeException(e);
        }
    }

    private Object getResultList(Query query, ReturnType returnType) {
        List list = query.getResultList();
        if (returnType.isProjection()) {
            return ((List<Tuple>) list).stream()
                .map(ProjectionProxy::new)
                .map(proxy -> Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class[] {returnType.getType()},
                    proxy
                )).collect(Collectors.toList());
        }

        return list;
    }

    private Object singleObject(Method method, Object[] args, ReturnType returnType, boolean rollbackIfNotFound) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query query = getQuery(method, session, returnType);
            passParameters(method, query, args);
            Object result;
            if (returnType.isVoid()) {
                result = query.executeUpdate();
            } else {
                result = getSingleResult(query, returnType);
            }
            ctx.commit();
            return result;
        } catch (NoResultException e) {
            if (rollbackIfNotFound) {
                ctx.rollback();
            }
            throw e;
        } catch (Exception e) {
            ctx.rollback();
            throw new RuntimeException(e);
        }
    }

    private Object getSingleResult(Query query, ReturnType returnType) {
        Object result = query.getSingleResult();
        if (returnType.isProjection()) {
            Tuple tuple = (Tuple) result;
            ProjectionProxy proxy = new ProjectionProxy(tuple);
            result = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {returnType.getType()},
                proxy
            );
        }

        return result;
    }

    private Query getQuery(Method method, Session session, ReturnType returnType) {
        Class<?> resultClass = returnType.getHibernateResultType();
        var metaQuery = method.getAnnotation(ru.finex.core.repository.Query.class);
        if (metaQuery == null) {
            var metaNamedQuery = method.getAnnotation(NamedQuery.class);
            if (metaNamedQuery == null || StringUtils.isBlank(metaNamedQuery.value())) {
                return session.createNamedQuery(interfaceType.getSimpleName() + "." + method.getName(), resultClass);
            }

            return session.createNamedQuery(metaNamedQuery.value(), resultClass);
        }

        return session.createQuery(metaQuery.value(), resultClass);
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

    private static Class<?> extractGeneric(Method method, int index) {
        ParameterizedType typeWithGeneric = (ParameterizedType) method.getGenericReturnType();
        return (Class<?>) typeWithGeneric.getActualTypeArguments()[0];
    }

    @RequiredArgsConstructor
    private static class ReturnType {
        @Getter
        private final Class<?> type;

        ReturnType(Method method) {
            this.type = extractGeneric(method, 0);
        }

        boolean isVoid() {
            return isVoid(type);
        }

        static boolean isVoid(Class<?> type) {
            return type == void.class;
        }

        boolean isOptional() {
            return isOptional(type);
        }

        static boolean isOptional(Class<?> type) {
            return type == Optional.class;
        }

        boolean isProjection() {
            return isProjection(type);
        }

        static boolean isProjection(Class<?> type) {
            return type.isAnnotationPresent(Projection.class);
        }

        boolean isFuture() {
            return isFuture(type);
        }

        static boolean isFuture(Class<?> type) {
            return Future.class.isAssignableFrom(type);
        }

        boolean isCollection() {
            return isCollection(type);
        }

        static boolean isCollection(Class<?> type) {
            return Collection.class.isAssignableFrom(type);
        }

        Class<?> getHibernateResultType() {
            return getHibernateResultType(type);
        }

        static Class<?> getHibernateResultType(Class<?> type) {
            if (isVoid(type)) {
                return null;
            }

            if (isProjection(type)) {
                return Tuple.class;
            }

            return type;
        }

    }

}
