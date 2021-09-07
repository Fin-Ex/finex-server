package ru.finex.gs.service;

import com.google.inject.ImplementedBy;
import ru.finex.gs.id.RuntimeIdServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(RuntimeIdServiceImpl.class)
public interface RuntimeIdService {

    int generateId();

}
