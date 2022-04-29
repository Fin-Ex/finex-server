package ru.finex.core.fsm.impl;

import ru.finex.core.fsm.FiniteStateMachine;
import ru.finex.core.fsm.State;

/**
 * Simple finite state machine.
 *
 * @param <E> owner
 * @param <S> state
 * @author m0nster.mind
 * @since wgp 19.09.2018
 */
public class StandardStateMachine<E, S extends State<E>> implements FiniteStateMachine<E, S> {

    protected E owner;
    protected S currentState;
    protected S prevState;

    public StandardStateMachine(E owner) {
        this.owner = owner;
    }

    @Override
    public void setInitialState(S state) {
        this.currentState = state;
        this.prevState = null;
    }

    @Override
    public void changeState(S state) {
        prevState = currentState;

        if (currentState != null) {
            currentState.exit(owner);
        }

        currentState = state;

        if (currentState != null) {
            currentState.enter(owner);
        }
    }

    @Override
    public boolean rollbackState() {
        if (prevState == null) {
            return false;
        }

        changeState(prevState);
        return true;
    }

    @Override
    public S getState() {
        return currentState;
    }

    @Override
    public boolean isInState(S state) {
        return this.currentState == state;
    }

    @Override
    public void update() {
        if (currentState != null) {
            currentState.update(owner);
        }
    }

}
