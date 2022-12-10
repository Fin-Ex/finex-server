package ru.finex.core.repository;

import jakarta.persistence.Column;

import java.lang.annotation.Annotation;

/**
 * @author m0nster.mind
 */
public class EmptyColumn implements Column {

    public static final Column INSTANCE = new EmptyColumn();

    @Override
    public String name() {
        return "";
    }

    @Override
    public boolean unique() {
        return false;
    }

    @Override
    public boolean nullable() {
        return true;
    }

    @Override
    public boolean insertable() {
        return true;
    }

    @Override
    public boolean updatable() {
        return true;
    }

    @Override
    public String columnDefinition() {
        return "";
    }

    @Override
    public String table() {
        return "";
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public int length() {
        return 255;
    }

    @Override
    public int precision() {
        return 0;
    }

    @Override
    public int scale() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Column.class;
    }

}
