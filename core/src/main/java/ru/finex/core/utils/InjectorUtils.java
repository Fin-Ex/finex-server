package ru.finex.core.utils;

import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.experimental.UtilityClass;
import ru.finex.core.GlobalContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m0nster.mind
 */
@UtilityClass
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public class InjectorUtils {

    public static List<Module> collectModules(Class<? extends Annotation> annType) {
        return collectModules("", annType);
    }

    public static List<Module> collectModules(String rootPackage, Class<? extends Annotation> annType) {
        return GlobalContext.reflections.getTypesAnnotatedWith(annType)
            .stream()
            .filter(e -> e.getCanonicalName().startsWith(rootPackage))
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !Modifier.isInterface(e.getModifiers()))
            .map(ClassUtils::createInstance)
            .map(e -> (Module) e)
            .collect(Collectors.toList());
    }

    public static Injector createChildInjector(String rootPackage, Class<? extends Annotation> annType, Injector parentInjector) {
        return parentInjector.createChildInjector(collectModules(rootPackage, annType));
    }

    public static Injector createChildInjector(Class<? extends Annotation> annType, Injector parentInjector) {
        return createChildInjector("", annType, parentInjector);
    }

}
