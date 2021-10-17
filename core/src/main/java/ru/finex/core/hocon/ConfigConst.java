package ru.finex.core.hocon;

import lombok.experimental.UtilityClass;

/**
 * @author m0nster.mind
 */
@UtilityClass
class ConfigConst {

    public static final String EXP_FIELD = "$$field$$";
    public static final String EXP_PACKAGE = "$$package$$";
    public static final String EXP_CLASSNAME = "$$classname$$";

    public static final String DEFAULT_PROPERTY = "$$field$$";
    public static final String DEFAULT_BASE_PATH = "$$package$$.$$classname$$";

}
