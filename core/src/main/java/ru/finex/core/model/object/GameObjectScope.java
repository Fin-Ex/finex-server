package ru.finex.core.model.object;

import com.google.inject.ImplementedBy;
import ru.finex.core.inject.scope.ScopeHandler;

/**
 * Скоуп игрового объекта.
 *
 * @author oracle
 */
@ImplementedBy(DefaultGameObjectScope.class)
public interface GameObjectScope<T extends GameObject> extends ScopeHandler<T> {

}
