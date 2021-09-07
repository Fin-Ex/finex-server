package ru.finex.core.hocon;

import lombok.experimental.UtilityClass;

/**
 * @author m0nster.mind
 */
@UtilityClass
class ConfigConst {

    public final static String EXP_FIELD = "$$field$$";
    public final static String EXP_PACKAGE = "$$package$$";
    public final static String EXP_CLASSNAME = "$$classname$$";

    public final static String DEFAULT_PROPERTY = "$$field$$";
    public final static String DEFAULT_BASE_PATH = "$$package$$.$$classname$$";

}
