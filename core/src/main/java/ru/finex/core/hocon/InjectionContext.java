package ru.finex.core.hocon;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author m0nster.mind
 */
@Data
class InjectionContext {

    private String property = ConfigConst.DEFAULT_PROPERTY;
    private String basePath = ConfigConst.DEFAULT_BASE_PATH;
    private boolean canMissing;
    private boolean nullable;

    public void replaceExpressions(Class<?> clazz, Field field) {
        property = replaceStringExpressions(property, clazz, field);
        basePath = replaceStringExpressions(basePath, clazz, field);
    }

    private static String replaceStringExpressions(String str, Class<?> clazz, Field field) {
        return str.replace(ConfigConst.EXP_CLASSNAME, clazz.getSimpleName())
            .replace(ConfigConst.EXP_FIELD, field.getName())
            .replace(ConfigConst.EXP_PACKAGE, clazz.getPackageName());
    }

}
