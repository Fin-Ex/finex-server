package ru.finex.core.inject.scope;

/**
 * A global interface that should be implemented by custom scopes.
 *
 * @author oracle
 */
public interface ScopeHandler<T> {

    /**
     * Enter to command scope.
     * Provides context to injector of object scope.
     * @param object an object that would like to be scoped object
     */
    void enterScope(T object);

    /**
     * Exit from object scope.
     * Removes context from injector of object scope.
     */
    void exitScope();

}
