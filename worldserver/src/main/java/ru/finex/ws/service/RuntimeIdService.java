package ru.finex.ws.service;

import com.google.inject.ImplementedBy;
import ru.finex.ws.id.RuntimeIdServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(RuntimeIdServiceImpl.class)
public interface RuntimeIdService {

    /**
     * Generates runtime ID.
     * @return runtime ID
     */
    int generateId();

}
