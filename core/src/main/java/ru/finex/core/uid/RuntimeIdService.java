package ru.finex.core.uid;

/**
 * @author m0nster.mind
 */
public interface RuntimeIdService {

    /**
     * Generates runtime ID.
     * @return runtime ID
     */
    int generateId();

    /**
     * Free runtime ID.
     * @param id runtime ID
     */
    void free(int id);

}
