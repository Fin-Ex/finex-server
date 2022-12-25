package ru.finex.core.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class GenericUtils {

    /**
     * Возвращает тип генерика, который указан у типа.
     * @param type класс у которого требуется взять генерик
     * @param order последовательный номер генерика
     * @return класс генерика или null
     * @throws IllegalArgumentException в случае, если order больше, чем количество генериков у класса
     */
    @SuppressWarnings({"checkstyle:JavadocMethod", "checkstyle:ParameterAssignment"})
    public static <T> Class<T> getGenericType(Class<?> type, int order) throws IllegalArgumentException {
        type = getProxyImplementation(type);

        Type[] generics = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        if (order >= generics.length) {
            throw new IllegalArgumentException();
        }

        try {
            return (Class<T>) Class.forName(generics[order].getTypeName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Возвращает тип генерика, который указан у типа.
     * @param type класс который реализует интерфейс
     * @param interfaceType интерфейс у которого требуется взять генерик
     * @param order последовательный номер генерика
     * @return класс генерика или null
     * @throws IllegalArgumentException в случае, если order больше, чем количество генериков у класса
     */
    @SuppressWarnings({"checkstyle:JavadocMethod", "checkstyle:ParameterAssignment"})
    public static <T> Class<T> getInterfaceGenericType(Class<?> type, Class<?> interfaceType, int order) throws IllegalArgumentException {
        type = getProxyImplementation(type);

        ParameterizedType parameterizedType = null;
        for (Type genericInterface : type.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType e) {
                if (e.getRawType() == interfaceType) {
                    parameterizedType = e;
                    break;
                }
            }
        }

        if (parameterizedType == null) {
            throw new IllegalArgumentException(String.format(
                "Class '%s' is not implement generic interface '%s'!",
                type.getCanonicalName(),
                interfaceType.getCanonicalName()
            ));
        }

        Type[] generics = parameterizedType.getActualTypeArguments();
        if (order >= generics.length) {
            throw new IllegalArgumentException();
        }

        try {
            return (Class<T>) Class.forName(generics[order].getTypeName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Class<?> getProxyImplementation(Class<?> clazz) {
        Class<?> type = clazz;
        while (type.getCanonicalName().contains("EnhancerByGuice")) {
            type = type.getSuperclass();
        }

        return type;
    }

}
