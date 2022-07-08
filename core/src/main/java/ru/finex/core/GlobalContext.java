package ru.finex.core;

import com.google.inject.Injector;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный контекст всего сервера.
 *
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
public class GlobalContext {

    @SuppressWarnings("checkstyle:ConstantName")
    public static final Map<Object, Object> context = new HashMap<>();
    public static Injector injector;
    public static Map<String, String> arguments;
    public static Reflections reflections;
    public static String rootPackage;

    /**
     * Clear global context.
     */
    public static void clear() {
        context.clear();
        injector = null;
        arguments = null;
        reflections = null;
        rootPackage = null;
    }

}
