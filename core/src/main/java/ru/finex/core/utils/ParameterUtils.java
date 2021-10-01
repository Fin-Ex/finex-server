package ru.finex.core.utils;

import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class ParameterUtils {

    public static List<Method> getMethodsWithParameterAnnotation(Class<?> type, Class<? extends Annotation> annotationType,
        boolean searchSupers, boolean ignoreAccess) {

        List<Class<?>> classes = searchSupers ? getAllSuperclassesAndInterfaces(type) : new ArrayList<>();
        classes.add(0, type);

        List<Method> result = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Method[] methods = ignoreAccess ? clazz.getDeclaredMethods() : clazz.getMethods();
            for (Method method : methods) {
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(annotationType)) {
                        result.add(method);
                        break;
                    }
                }
            }
        }

        return result;
    }

    private static List<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<>();
        final List<Class<?>> allSuperclasses = org.apache.commons.lang3.ClassUtils.getAllSuperclasses(cls);
        int superClassIndex = 0;
        final List<Class<?>> allInterfaces = org.apache.commons.lang3.ClassUtils.getAllInterfaces(cls);
        int interfaceIndex = 0;
        while (interfaceIndex < allInterfaces.size() ||
            superClassIndex < allSuperclasses.size()) {
            final Class<?> acls;
            if (interfaceIndex >= allInterfaces.size()) {
                acls = allSuperclasses.get(superClassIndex++);
            } else if ((superClassIndex >= allSuperclasses.size()) || (interfaceIndex < superClassIndex) || !(superClassIndex < interfaceIndex)) {
                acls = allInterfaces.get(interfaceIndex++);
            } else {
                acls = allSuperclasses.get(superClassIndex++);
            }
            allSuperClassesAndInterfaces.add(acls);
        }
        return allSuperClassesAndInterfaces;
    }

}
