package ru.finex.core.db.migration.impl;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import ru.finex.evolution.ClasspathScanner;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ClasspathScannerImpl implements ClasspathScanner {

    private final Reflections reflections;

    @Override
    public Collection<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    @Override
    public Collection<String> getResources(Pattern pattern) {
        return reflections.getResources(pattern);
    }

}
