package ru.finex.core.id;

import com.google.inject.ImplementedBy;
import ru.finex.core.id.impl.RuntimeIdServiceImpl;

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
