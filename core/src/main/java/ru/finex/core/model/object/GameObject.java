package ru.finex.core.model.object;

/**
 * @author m0nster.mind
 */
public interface GameObject {

    /**
     * Game object runtime ID.
     * @return runtime ID
     */
    int getRuntimeId();

    /**
     * Game object persistence ID.
     * @return persistence ID
     */
    int getPersistenceId();

}
