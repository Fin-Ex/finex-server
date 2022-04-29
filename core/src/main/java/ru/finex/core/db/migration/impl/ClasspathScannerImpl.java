package ru.finex.core.db.migration.impl;

import ru.finex.core.GlobalContext;
import ru.finex.evolution.ClasspathScanner;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClasspathScannerImpl implements ClasspathScanner {

    @Override
    public Collection<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return GlobalContext.reflections.getTypesAnnotatedWith(annotation);
    }

    @Override
    public Collection<String> getResources(Pattern pattern) {
        return GlobalContext.reflections.getResources(pattern);
    }

}
