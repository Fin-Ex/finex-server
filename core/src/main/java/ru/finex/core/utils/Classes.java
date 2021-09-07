/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.finex.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pointer*Rage
 */
public class Classes {
	/** Class.forName with runtime exception */
	public static Class<?> getClass(String path) throws RuntimeException {
		try {
			return Class.forName(path);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object createInstance(Class<?> clazz) {
		Object object = singletonInstance(clazz);
		if (object == null) {
			try {
				return clazz.getConstructor().newInstance();
			} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e1) {
				// log.info("failed create class, method not found and constructor not found.
				// {}", clazz.getName(), e);
				return null;
			}
		}

		return object;
	}

	public static Object singletonInstance(Class<?> clazz) {
		final Method method;
		try {
			method = clazz.getDeclaredMethod("getInstance");
		} catch (NoSuchMethodException e) {
			return null;
		}

		try {
			return method.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
				if (e.name().toLowerCase().equals(val.toLowerCase())) {
					return e;
				}
			}
			throw new RuntimeException("Enum constant not found");
		} else
			throw new RuntimeException("Unknown default type");
	}

//	public static Object transformToTypeEx(String val, Class<?> type) {
//		if(type.isAssignableFrom(List.class)) {
//			Type generic;
//			try {
//				generic = type.getGenericSuperclass();
//			} catch(TypeNotPresentException e) {
//				throw new RuntimeException("Generic type of list not present", e);
//			}
//			
//			List list = new ArrayList<>();
//			Object object;
//			try {
//				
//			} catch(RuntimeException e) {
//				
//			}
//			
//		}
//	}

	/**
	 * return first class from hierarchy annotated specified annotation. if classes
	 * not annotated, return null
	 */
	public static <T extends Annotation> Class<?> getAnnotatedClass(Class<?> clazz, Class<T> annotationClass) {
		if (clazz.isAnnotationPresent(annotationClass)) {
			return clazz;
		}

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return null;
		}

		return getAnnotatedClass(superClass, annotationClass);
	}

	/**
	 * return specified annotation from first class annotated in class hierarchy. if
	 * classes not annotated, return null
	 */
	public static <T extends Annotation> T[] getAnnotationClass(Class<?> clazz, Class<T> annotationClass) {
		T[] annotations = clazz.getAnnotationsByType(annotationClass);
		if (annotations.length > 0) {
			return annotations;
		}

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return null;
		}

		return getAnnotationClass(superClass, annotationClass);
	}

	public static <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> clazz, Class<T> annotationClass) {
		List<Method> list = new ArrayList<>();
		getAnnotatedMethods(clazz, annotationClass, list);
		return list;
	}

	public static <T extends Annotation> void getAnnotatedMethods(Class<?> clazz, Class<T> annotationClass,
			List<Method> out) {
		Method[] methods = clazz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			if (method.isAnnotationPresent(annotationClass)) {
				out.add(method);
			}
		}

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return;
		}

		getAnnotatedMethods(superClass, annotationClass, out);
	}

	public static <T extends Annotation> List<T> getAnnotationMethod(Class<?> clazz, Class<T> annotationClass) {
		List<T> list = new ArrayList<>();
		getAnnotationMethod(clazz, annotationClass, list);
		return list;
	}

	public static <T extends Annotation> void getAnnotationMethod(Class<?> clazz, Class<T> annotationClass,
			List<T> out) {
		Method[] methods = clazz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			final T[] annotations = method.getAnnotationsByType(annotationClass);

			for (int j = 0; j < annotations.length; j++) {
				out.add(annotations[j]);
			}
		}

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return;
		}

		getAnnotationMethod(superClass, annotationClass, out);
	}

	public static <T extends Annotation> List<Field> getAnnotatedField(Class<?> clazz, Class<T> annotationClass) {
		List<Field> list = new ArrayList<>();
		getAnnotatedField(clazz, annotationClass, list);
		return list;
	}

	public static <T extends Annotation> void getAnnotatedField(Class<?> clazz, Class<T> annotationClass,
			List<Field> out) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			final Field field = fields[i];
			if (field.isAnnotationPresent(annotationClass)) {
				out.add(field);
			}
		}

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return;
		}

		getAnnotatedField(superClass, annotationClass, out);
	}

	public static <T extends Annotation> List<T> getAnnotationField(Class<?> clazz, Class<T> annotationClass) {
		List<T> list = new ArrayList<>();
		getAnnotationField(clazz, annotationClass, list);
		return list;
	}

	public static <T extends Annotation> void getAnnotationField(Class<?> clazz, Class<T> annotationClass,
			List<T> out) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			final Field field = fields[i];
			final T[] annotations = field.getAnnotationsByType(annotationClass);

			for (int j = 0; j < annotations.length; j++) {
				out.add(annotations[j]);
			}
		}

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return;
		}

		getAnnotationField(superClass, annotationClass, out);
	}

	public static List<Class<?>> getAllParents(Class<?> clazz) {
		List<Class<?>> list = new ArrayList<>();
		getAllParents(clazz, list);
		return list;
	}

	public static void getAllParents(Class<?> clazz, List<Class<?>> out) {
		out.add(clazz);

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return;
		}

		getAllParents(superClass, out);
	}
}
