package ru.finex.core;

import com.google.inject.Injector;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

/**
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

}
