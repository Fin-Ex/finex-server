package ru.finex.core.model.object.scope;

import com.google.inject.ImplementedBy;
import ru.finex.core.inject.scope.ScopeHandler;
import ru.finex.core.model.object.scope.impl.DefaultGameObjectScope;
import ru.finex.core.model.object.GameObject;

/**
 * Скоуп игрового объекта.
 *
 * @author oracle
 */
@ImplementedBy(DefaultGameObjectScope.class)
public interface GameObjectScope<T extends GameObject> extends ScopeHandler<T> {

}
