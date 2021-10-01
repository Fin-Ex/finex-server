package ru.finex.core.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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

    public static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    public static Object createInstance(Class<?> clazz, Object... params) throws RuntimeException {
        if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
            throw new RuntimeException("Class " + clazz.getCanonicalName() + " is interface or abstract!");
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length < 1) {
            throw new RuntimeException("Class " + clazz.getCanonicalName() + " doesnt have public constructors");
        }

        constructorLabel: for (int i = 0; i < constructors.length; i++) {
            final Constructor<?> constructor = constructors[i];

            Class<?>[] parameters = constructor.getParameterTypes();
            if (parameters.length != params.length) {
                continue;
            }

            if (parameters.length < 1) {
                try {
                    return constructor.newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int j = 0; j < params.length; j++) {
                final Object param = params[j];
                if (param == null) {
                    continue;
                }

                final Class<?> parameter = parameters[j];
                final Class<?> paramClass = param.getClass();
                if (parameter.isPrimitive() && !paramClass.isPrimitive() && isPrimitiveWrap(paramClass, parameter)) {
                    params[j] = transformStringToType(param.toString(), parameter);
                    continue;
                }

                try {
                    param.getClass().asSubclass(parameter);
                } catch (ClassCastException e) {
                    continue constructorLabel;
                }
            }

            try {
                return constructor.newInstance(params);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Not found constructor with specified params. ");
        sb.append("Class: ").append(clazz.getCanonicalName()).append("; ");
        sb.append("Params: ");
        for (int i = 0; i < params.length; i++) {
            final Object param = params[i];
            sb.append(param == null ? "null" : param.getClass().getSimpleName());
            if (i != params.length - 1) {
                sb.append(", ");
            }
        }

        throw new RuntimeException(sb.toString());
    }

    public static boolean isPrimitiveWrap(Class<?> clazz, Class<?> primitiveClass) {
        try {
            Field field = clazz.getDeclaredField("TYPE");
            field.setAccessible(true);
            Class<?> type = (Class<?>) field.get(null);
            return type == primitiveClass || primitiveClass.isAssignableFrom(clazz);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    /** primitives, primitives arrays, enums. exclude string arrays */
    public static Object transformStringToType(String val, Class<?> type) {
        if (type.isAssignableFrom(String.class)) {
            return val;
        } else if (type.isAssignableFrom(Byte.TYPE) || type.isAssignableFrom(Byte.class)
            || type.isAssignableFrom(byte.class)) {
            return Byte.parseByte(val);
        } else if (type.isAssignableFrom(Character.TYPE) || type.isAssignableFrom(Character.class)
            || type.isAssignableFrom(char.class)) {
            return val.toCharArray()[0];
        } else if (type.isAssignableFrom(Short.TYPE) || type.isAssignableFrom(Short.class)
            || type.isAssignableFrom(short.class)) {
            return Short.parseShort(val);
        } else if (type.isAssignableFrom(Integer.TYPE) || type.isAssignableFrom(Integer.class)
            || type.isAssignableFrom(int.class)) {
            return Integer.parseInt(val);
        } else if (type.isAssignableFrom(Long.TYPE) || type.isAssignableFrom(Long.class)
            || type.isAssignableFrom(long.class)) {
            return Long.parseLong(val);
        } else if (type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Float.class)
            || type.isAssignableFrom(float.class)) {
            return Float.parseFloat(val);
        } else if (type.isAssignableFrom(Double.TYPE) || type.isAssignableFrom(Double.class)
            || type.isAssignableFrom(double.class)) {
            return Double.parseDouble(val);
        } else if (type.isAssignableFrom(Boolean.TYPE) || type.isAssignableFrom(Boolean.class)
            || type.isAssignableFrom(boolean.class)) {
            return Boolean.parseBoolean(val);
        } else if (type.isAssignableFrom(Byte[].class) || type.isAssignableFrom(byte[].class)) {
            String[] values = val.split("\\s|,|;");
            byte[] array = new byte[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Byte.parseByte(values[i]);
            }
            return array;
        } else if (type.isAssignableFrom(Short[].class) || type.isAssignableFrom(short[].class)) {
            String[] values = val.split("\\s|,|;");
            short[] array = new short[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Short.parseShort(values[i]);
            }
            return array;
        } else if (type.isAssignableFrom(Integer[].class) || type.isAssignableFrom(int[].class)) {
            String[] values = val.split("\\s|,|;");
            int[] array = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Integer.parseInt(values[i]);
            }
            return array;
        } else if (type.isAssignableFrom(Long[].class) || type.isAssignableFrom(long[].class)) {
            String[] values = val.split("\\s|,|;");
            long[] array = new long[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Long.parseLong(values[i]);
            }
            return array;
        } else if (type.isAssignableFrom(Float[].class) || type.isAssignableFrom(float[].class)) {
            String[] values = val.split("\\s|,|;");
            float[] array = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Float.parseFloat(values[i]);
            }
            return array;
        } else if (type.isAssignableFrom(Double[].class) || type.isAssignableFrom(double[].class)) {
            String[] values = val.split("\\s|,|;");
            double[] array = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Double.parseDouble(values[i]);
            }
            return array;
        } else if (type.isAssignableFrom(Boolean[].class) || type.isAssignableFrom(boolean[].class)) {
            String[] values = val.split("\\s|,|;");
            boolean[] array = new boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = Boolean.parseBoolean(values[i]);
            }
            return array;
        } else if (type.isEnum()) {
            Class<? extends Enum> clazz = type.asSubclass(Enum.class);
            Enum[] constants = clazz.getEnumConstants();
            for (Enum e : constants) {
                if (e.name().equalsIgnoreCase(val)) {
                    return e;
                }
            }
            throw new RuntimeException("Enum constant not found");
        } else
            throw new RuntimeException("Unknown default type");
    }

}
