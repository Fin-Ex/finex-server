package ru.finex.core;

import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import ru.finex.core.hocon.ApplicationConfigProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author m0nster.mind
 */
public class ServerRule implements TestRule {

    private final String config;
    private final Module[] modules;

    public ServerRule(String config, Module...modules) {
        this.config = config;
        this.modules = modules;
    }

    public ServerRule(String config) {
        this.config = config;
        this.modules = new Module[0];
    }

    public ServerRule(Module...modules) {
        this.config = null;
        this.modules = modules;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.setProperty("MANAGEMENT_ENABLE", "false"); // disable hawtio to speed-up server up and down

                List<String> args = new ArrayList<>();
                Optional.ofNullable(config)
                    .map(e -> String.join("=", ApplicationConfigProvider.CONFIG_ARG, e))
                    .ifPresent(args::add);

                Stream.of(modules)
                    .map(Module::getCanonicalPath)
                    .reduce((e1, e2) -> String.join(";", e1, e2))
                    .map(e -> String.join("=", ServerApplication.MODULES_ARG, e))
                    .ifPresent(args::add);

                ServerApplication.start("ru.finex", args.toArray(new String[0]));
                try {
                    base.evaluate();
                } finally {
                    Injector injector = GlobalContext.injector;
                    if (injector != null) {
                        SigtermListener sigtermListener = injector.getInstance(SigtermListener.class);
                        sigtermListener.run();
                    }
                }
            }
        };
    }

    @Data
    public static class Module {
        private final String canonicalPath;

        public Module(Class<? extends com.google.inject.Module> moduleType) {
            canonicalPath = moduleType.getCanonicalName();
        }

        public static Module of(Class<? extends com.google.inject.Module> moduleType) {
            return new Module(moduleType);
        }
    }

}
