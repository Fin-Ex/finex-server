package ru.finex.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.mycila.guice.ext.closeable.CloseableModule;
import com.mycila.guice.ext.jsr250.Jsr250Module;
import com.mycila.jmx.JmxModule;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.logback.LogbackConfiguration;
import ru.finex.core.utils.InjectorUtils;
import ru.finex.evolution.Evolution;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Производит инициализацию ядра.
 *
 * @author m0nster.mind
 */
@Evolution("core")
public class ServerApplication {

    /**
     * Производит инициализацию ядра:
     * <ul>
     * <li> Настраивает логирование</li>
     * <li> Инициализирует {@link GlobalContext глобальный контекст}</li>
     * <li> Иницилазация модулей</li>
     * <li> Создаёт инжектор</li>
     * <li> Регистрирует {@link SigtermListener}</li>
     * <li> Уведомляет {@link ApplicationBuilt подписчиков} о старте.</li>
     * </ul>
     *
     * @param modulePackage root package сервера
     * @param args аргументы запуска
     */
    public static void start(String modulePackage, String[] args) {
        saveArguments(args);

        LogbackConfiguration logbackConfiguration = new LogbackConfiguration();
        logbackConfiguration.configureLogback();

        GlobalContext.rootPackage = modulePackage;
        GlobalContext.reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forJavaClassPath())
            .addScanners(
                Scanners.SubTypes,
                Scanners.TypesAnnotated,
                Scanners.MethodsAnnotated,
                Scanners.ConstructorsAnnotated,
                Scanners.FieldsAnnotated,
                Scanners.Resources
            )
        );

        Banner.print();
        LoggerFactory.getLogger(ServerApplication.class)
            .info("Core version: {}", Version.getImplVersion());

        List<Module> modules = new ArrayList<>();
        modules.add(new CloseableModule());
        modules.add(new Jsr250Module());
        modules.add(new JmxModule());
        modules.addAll(InjectorUtils.collectModules(ServerApplication.class.getPackageName(), LoaderModule.class));
        modules.addAll(InjectorUtils.collectModules(modulePackage, LoaderModule.class));

        Injector globalInjector = Guice.createInjector(Stage.PRODUCTION, modules);
        GlobalContext.injector = globalInjector;

        SigtermListener sigtermListener = globalInjector.getInstance(SigtermListener.class);
        Runtime.getRuntime().addShutdownHook(new Thread(sigtermListener));

        GlobalContext.reflections.getSubTypesOf(ApplicationBuilt.class)
            .stream()
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !Modifier.isInterface(e.getModifiers()))
            .map(globalInjector::getInstance)
            .forEach(ApplicationBuilt::onApplicationBuilt);
    }

    private static void saveArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] pair = arg.split("=", 1);
                arguments.put(pair[0], pair[1]);
            } else {
                arguments.put(arg, arg);
            }
        }
        GlobalContext.arguments = arguments;
    }

}
