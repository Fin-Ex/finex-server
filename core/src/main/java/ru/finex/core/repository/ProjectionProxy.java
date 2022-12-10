package ru.finex.core.repository;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.NamingBase;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ProjectionProxy implements InvocationHandler {

    private static final NamingBase NAMING_BASE = new SnakeCaseStrategy();

    private final Tuple projection;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            Constructor<Lookup> ctor = Lookup.class.getDeclaredConstructor(Class.class);
            ctor.setAccessible(true);
            return ctor.newInstance(method.getDeclaringClass())
                .unreflectSpecial(method, method.getDeclaringClass())
                .bindTo(proxy)
                .invokeWithArguments(args);
        }

        if (method.getDeclaringClass() == Object.class) {
            return objectMethods(proxy, method, args);
        }

        Column column = Optional.ofNullable(method.getAnnotation(Column.class))
            .orElse(EmptyColumn.INSTANCE);

        String columnName = column.name();
        if (StringUtils.isBlank(columnName)) {
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                columnName = methodName.substring(3);
            } else {
                columnName = methodName;
            }

            columnName = NAMING_BASE.translate(columnName);
        }

        Object value = projection.get(columnName);
        if (value == null && !column.nullable()) {
            throw new NullPointerException("Column '" + columnName + "' is null!");
        }

        if (value != null) {
            value = postProcessValue(method, value);
        }

        return value;
    }

    private Object postProcessValue(Method method, Object value) {
        Object processed = value;
        if (Enum.class.isAssignableFrom(method.getReturnType())) {
            Enumerated enumerated = Optional.ofNullable(method.getAnnotation(Enumerated.class))
                .orElse(EmptyEnumerated.INSTANCE);

            Class<? extends Enum> returnType = (Class<? extends Enum>) method.getReturnType();

            EnumType type = enumerated.value();
            processed = switch (type) {
                case ORDINAL -> returnType.getEnumConstants()[((Number)value).intValue()];
                case STRING -> Enum.valueOf(returnType, value.toString());
                default -> value;
            };
        }

        return processed;
    }

    @SuppressWarnings("checkstyle:IllegalThrows")
    private Object objectMethods(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString") && args == null && method.getReturnType() == String.class) {
            return projection.toString();
        }

        if (method.getName().equals("hashCode") && args == null && method.getReturnType() == int.class) {
            return projection.hashCode();
        }

        Constructor<Lookup> ctor = Lookup.class.getDeclaredConstructor(Class.class);
        ctor.setAccessible(true);
        return ctor.newInstance(method.getDeclaringClass())
            .unreflectSpecial(method, method.getDeclaringClass())
            .bindTo(proxy)
            .invokeWithArguments(args);
    }

}
