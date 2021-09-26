package ru.finex.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.finex.core.db.migration.Evolution;
import ru.finex.core.inject.InjectedModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.logback.LogbackConfiguration;
import ru.finex.core.utils.InjectorUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author m0nster.mind
 */
@Evolution("core")
public class ServerApplication {

    public static void start(String modulePackage, String[] args) {
        saveArguments(args);

        LogbackConfiguration logbackConfiguration = new LogbackConfiguration();
        logbackConfiguration.configureLogback();

        GlobalContext.rootPackage = modulePackage;
        GlobalContext.reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forJavaClassPath())
            .addScanners(Scanners.Resources)
        );

        List<Module> modules = new ArrayList<>();
        modules.addAll(InjectorUtils.collectModules(ServerApplication.class.getPackageName(), LoaderModule.class));
        modules.addAll(InjectorUtils.collectModules(modulePackage, LoaderModule.class));
        Injector globalInjector = Guice.createInjector(Stage.PRODUCTION, modules);
        GlobalContext.injector = globalInjector;

        ServerContext serverContext = globalInjector.getInstance(ServerContext.class);
        Injector serverInjector = InjectorUtils.createChildInjector(modulePackage, InjectedModule.class, globalInjector);
        serverContext.setInjector(serverInjector);

        GlobalContext.reflections.getSubTypesOf(ApplicationBuilt.class)
            .stream()
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !Modifier.isInterface(e.getModifiers()))
            .map(serverInjector::getInstance)
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
