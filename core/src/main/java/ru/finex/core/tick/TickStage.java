package ru.finex.core.tick;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static ru.finex.core.tick.TickLevel.LEVEL_INPUT;
import static ru.finex.core.tick.TickLevel.LEVEL_PHYSICS;
import static ru.finex.core.tick.TickLevel.LEVEL_UPDATE;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
@RequiredArgsConstructor
public enum TickStage {
    PRE_INPUT(LEVEL_INPUT),
    INPUT(LEVEL_INPUT),
    POST_INPUT(LEVEL_INPUT),
    PRE_PHYSICS(LEVEL_PHYSICS),
    PHYSICS(LEVEL_PHYSICS),
    POST_PHYSICS(LEVEL_PHYSICS),
    PRE_UPDATE(LEVEL_UPDATE),
    UPDATE(LEVEL_UPDATE),
    POST_UPDATE(LEVEL_UPDATE);

    public static final TickStage[] INPUT_STAGES = {
        PRE_INPUT,
        INPUT,
        POST_INPUT
    };

    public static final TickStage[] PHYSICS_STAGES = {
        PRE_PHYSICS,
        PHYSICS,
        POST_PHYSICS
    };

    public static final TickStage[] UPDATE_STAGES = {
        PRE_UPDATE,
        UPDATE,
        POST_UPDATE
    };

    @Getter
    private final int level;

    public static int count() {
        return TickStage.values().length;
    }

}
