package ru.finex.core.command;

/**
 * @author m0nster.mind
 */
public interface Command extends Comparable<Command> {

    void executeCommand();

    int getPriority();

    @Override
    default int compareTo(Command o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
