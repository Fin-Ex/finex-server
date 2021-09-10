package ru.finex.ws.tick;

import lombok.experimental.UtilityClass;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class TickPriority {

    public static final int PRE_INPUT = 100;
    public static final int INPUT = 200;
    public static final int POST_INPUT = 300;

    public static final int PRE_PHYSICS = 400;
    public static final int PHYSICS = 500;
    public static final int POST_PHYSICS = 600;

    public static final int PRE_UPDATE = 700;
    public static final int UPDATE = 800;
    public static final int POST_UPDATE = 900;

}
