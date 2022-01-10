package ru.finex.core.fsm;

/**
 * Finite state machine state.
 *
 * @param <E> owner
 * @author m0nster.mind
 * @since wgp 19.09.2018
 */
public interface State<E> {

    /**
     * Enter to this state.
     * @param owner FSM owner
     */
    void enter(E owner);

    /**
     * Exit from this state.
     * @param owner FSM owner
     */
    void exit(E owner);

    /**
     * Tick event.
     * @param owner FSM owner
     */
    void update(E owner);

}
