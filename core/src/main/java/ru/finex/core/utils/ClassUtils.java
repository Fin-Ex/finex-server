package ru.finex.core.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class ClassUtils {

    /**
     * Find class with specified canonical name.
     * @param name canonical name of class
     * @return class or throw {@link RuntimeException}
     * @see Class#forName(String)
     */
    public static Class<?> forName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Class: '%s' not found!", name), e);
        }
    }

    /**
     * Конвертирует имя класса и поля в текстовую репрезентацию.
     * Формат: canonicalClassName::fieldName
     * @param clazz класс
     * @param field поле
     * @return текстовая репрезентация
     */
    public static String toStringClassAndField(Class<?> clazz, Field field) {
        return clazz.getCanonicalName() + "::" + field.getName();
    }

    /**
     * Возвращает тип генерика, который указан у типа.
     * @param type класс у которого требуется взять генерик
     * @param order последовательный номер генерика
     * @return класс генерика или null
     * @throws IllegalArgumentException в случае, если order больше, чем количество генериков у класса
     */
    public static <T> Class<T> getGenericType(Class<?> type, int order) throws IllegalArgumentException {
        while (type.getCanonicalName().contains("EnhancerByGuice")) {
            type = type.getSuperclass();
        }

        Type[] generics = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        if (generics.length < order) {
            throw new IllegalArgumentException();
        }

        try {
            return (Class<T>) Class.forName(generics[order].getTypeName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
