package ru.finex.testing.server;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.finex.testing.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Qualifier;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

/**
 * @author m0nster.mind
 */
public class ServerExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, ParameterResolver {

    private static final String CONFIG_ARG = "config";
    private static final String MODULES_ARG = "modules";

    private static final ExtensionContext.Namespace SERVER = create("ru.finex.server");
    private static final String INJECTOR = "injector";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        List<String> args = buildArguments(context);
        System.setProperty("MANAGEMENT_ENABLE", "false"); // disable hawtio to speed-up server up and down

        Class<?> serverApplication = ClassUtils.getClass("ru.finex.core.ServerApplication");
        Method start = MethodUtils.getAccessibleMethod(serverApplication, "start", String.class, String[].class);
        start.invoke(null, "ru.finex", args.toArray(new String[0]));
        saveInjector(context);
    }

    private List<String> buildArguments(ExtensionContext context) {
        return Utils.findAnnotationRecursive(context, Server.class)
                .map(server ->
                    Stream.of(
                        Optional.ofNullable(server.config())
                            .map(e -> String.join("=", CONFIG_ARG, e))
                            .orElse(null),
                        Stream.of(server.modules())
                            .map(Class::getCanonicalName)
                            .reduce((e1, e2) -> String.join(";", e1, e2))
                            .map(e -> String.join("=", MODULES_ARG, e))
                            .orElse(null)
                    ).filter(Objects::nonNull)
                    .collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Injector injector = getInjector(context);
        context.getRequiredTestInstances().getAllInstances()
                .forEach(injector::injectMembers);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        Injector injector = getInjector(context);
        Class<?> sigtermListenerClass = ClassUtils.getClass("ru.finex.core.SigtermListener");
        Runnable sigtermListener = (Runnable) injector.getInstance(sigtermListenerClass);
        sigtermListener.run();
        context.getStore(SERVER).remove(INJECTOR);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var executable = parameterContext.getDeclaringExecutable();
        return executable.isAnnotationPresent(TestInject.class) || parameterContext.isAnnotated(TestInject.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        Key key = getQualifier(parameter)
            .map(qualifier -> (Key) Key.get(
                TypeLiteral.get(parameter.getParameterizedType()),
                qualifier
            )).orElseGet(() -> Key.get(parameter.getParameterizedType()));

        Injector injector = getInjector(extensionContext);
        return injector.getInstance(key);
    }

    private Optional<Annotation> getQualifier(AnnotatedElement element) {
        Annotation[] annotations = element.getAnnotations();
        for (var annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                return Optional.of(annotation);
            }
        }

        return Optional.empty();
    }

    @SneakyThrows
    private static void saveInjector(ExtensionContext context) {
        Class<?> globalCtxClass = ClassUtils.getClass("ru.finex.core.GlobalContext");
        Field injectorField = FieldUtils.getField(globalCtxClass, "injector");
        context.getStore(SERVER).put(INJECTOR, FieldUtils.readField(injectorField, (Object) null));
    }

    private static Injector getInjector(ExtensionContext context) {
        return context.getStore(SERVER).get(INJECTOR, Injector.class);
    }
}
