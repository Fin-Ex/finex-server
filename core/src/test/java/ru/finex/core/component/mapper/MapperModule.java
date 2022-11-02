package ru.finex.core.component.mapper;

import com.google.inject.AbstractModule;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.core.pool.PoolService;
import ru.finex.core.uid.RuntimeIdService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author m0nster.mind
 */
public class MapperModule extends AbstractModule {

    @Override
    protected void configure() {
        var rIdService = mock(RuntimeIdService.class);
        when(rIdService.generateId()).thenReturn(1);
        doNothing().when(rIdService).free(eq(1));
        bind(RuntimeIdService.class).toInstance(rIdService);

        var poolService = mock(PoolService.class);
        when(poolService.getObject(any())).thenReturn(null);
        bind(PoolService.class).toInstance(poolService);

        var persistenceService = mock(GameObjectPersistenceService.class);
        doNothing().when(persistenceService).restore(any());
        bind(GameObjectPersistenceService.class).toInstance(persistenceService);
    }

}
