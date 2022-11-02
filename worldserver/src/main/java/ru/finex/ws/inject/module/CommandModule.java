package ru.finex.ws.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.ws.command.PhysicsCommandQueue;
import ru.finex.ws.command.UpdateCommandQueue;
import ru.finex.ws.command.impl.physics.PhysicsCommandQueueImpl;
import ru.finex.ws.command.impl.update.UpdateCommandQueueImpl;

/**
 * @author m0nster.mind
 */
public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PhysicsCommandQueue.class).to(PhysicsCommandQueueImpl.class);
        bind(UpdateCommandQueue.class).to(UpdateCommandQueueImpl.class);
    }

}
