package ru.finex.core;

import com.google.inject.Injector;
import lombok.Data;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import ru.finex.core.hocon.ApplicationConfigProvider;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.JacksonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    public static ServerRuleBuilder builder() {
        return new ServerRuleBuilder();
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

    public static class ServerRuleBuilder {
        private final List<Module> modules = new ArrayList<>();
        private String configPath;

        public ServerRuleBuilder configPath(String configPath) {
            this.configPath = configPath;
            return this;
        }

        public ServerRuleBuilder module(Module module) {
            modules.add(module);
            return this;
        }

        public ServerRuleBuilder modules(Module...modules) {
            this.modules.addAll(Arrays.asList(modules));
            return this;
        }

        public ServerRuleBuilder databaseModule() {
            modules.add(Module.of(DbModule.class));
            return this;
        }

        public ServerRuleBuilder configModule() {
            modules.add(Module.of(HoconModule.class));
            return this;
        }

        public ServerRuleBuilder clusterModule() {
            modules.add(Module.of(ClusterModule.class));
            return this;
        }

        public ServerRuleBuilder jsonModule() {
            modules.add(Module.of(JacksonModule.class));
            return this;
        }

        public ServerRule build() {
            return new ServerRule(configPath, modules.toArray(new Module[0]));
        }
    }

}
