package ru.finex.testing;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.util.Optional;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class Utils {

    /**
     * Find recursive annotation/meta-annotation/inherited-annotation.
     * @param context junit extension context
     * @param type annotation class
     * @param <T> annotation type
     * @return optional annotation
     */
    public static <T extends Annotation> Optional<T> findAnnotationRecursive(ExtensionContext context, Class<T> type) {
        ExtensionContext currentContext = context;
        Optional<T> annotation;

        do {
            annotation = findAnnotation(currentContext.getElement(), type);
            if (!currentContext.getParent().isPresent()) {
                break;
            }

            currentContext = currentContext.getParent().get();
        } while (!annotation.isPresent() && currentContext != context.getRoot());

        return annotation;
    }

}
