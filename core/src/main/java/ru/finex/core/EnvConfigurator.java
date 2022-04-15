package ru.finex.core;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Singleton;

/**
 * Конфигуратор для {@code environments} сервера.
 * Пример плэйсхолдера: ${USER_NAME}
 *
 * @author finfan
 */
@Singleton
public class EnvConfigurator {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([\\w\\d_]+)}");

    /**
     * Подменяет плэйсхолдеры на значения из {@code environments}.
     * 
     * @param configuration контейнер в котором хранятся подменяемые значения
     */
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public void configure(Map<Object, Object> configuration) {
        configuration.entrySet().forEach(entry -> {
            String value = entry.getValue().toString();
            Matcher matcher = PATTERN.matcher(value);
            while (matcher.find()) {
                String envName = matcher.group(1);
                String envValue = Matcher.quoteReplacement(System.getenv().get(envName));
                entry.setValue(matcher.replaceFirst(Objects.requireNonNullElse(envValue, StringUtils.EMPTY)));
            }
        });
    }

}
