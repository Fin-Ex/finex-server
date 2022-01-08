package ru.finex.core.fsm;

/**
 * Base interface of any finite state machine.
 *
 * @param <E> owner
 * @param <S> state
 * @author m0nster.mind
 * @since wgp 19.09.2018
 */
public interface FiniteStateMachine<E, S extends State<E>> {

    /**
     * Setup initial FSM state.
     * @param state state
     */
    void setInitialState(S state);

    /**
     * Change state to specified.
     * @param state state
     */
    void changeState(S state);

    /**
     * Cancel current state and move to previous.
     * @return true if state rollback, otherwise false
     */
    boolean rollbackState();

    /**
     * Get current state.
     * @return state
     */
    S getState();

    /**
     * Test FSM to running in specified state.
     * @param state state
     * @return true if FSM running in specified state, otherwise false
     */
    boolean isInState(S state);

    /**
     * Tick event.
     */
    void update();

}
