package ru.finex.core.model.object.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.object.GameObject;
import ru.finex.core.model.object.scope.GameObjectScoped;

/**
 * @author m0nster.mind
 */
@Getter
@GameObjectScoped
@RequiredArgsConstructor
public class GameObjectImpl implements GameObject {

    /** Динамический ID игрового объекта, который существует только в рамках сессии этого игрового объекта. */
    private final int runtimeId;
    /** Персистентный ID игрового объекта, который хранится в БД. */
    private final int persistenceId;

}
