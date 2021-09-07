package ru.finex.core.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

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

}
