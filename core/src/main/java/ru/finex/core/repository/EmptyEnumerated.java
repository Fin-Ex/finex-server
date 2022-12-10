package ru.finex.core.repository;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.lang.annotation.Annotation;

/**
 * @author m0nster.mind
 */
public class EmptyEnumerated implements Enumerated {

    public static final Enumerated INSTANCE = new EmptyEnumerated();

    @Override
    public EnumType value() {
        return EnumType.ORDINAL;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Enumerated.class;
    }

}
