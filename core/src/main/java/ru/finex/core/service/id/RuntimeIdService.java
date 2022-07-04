package ru.finex.core.service.id;

import com.google.inject.ImplementedBy;
import ru.finex.core.service.id.impl.RuntimeIdServiceImpl;

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
