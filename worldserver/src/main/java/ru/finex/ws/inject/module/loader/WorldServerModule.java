package ru.finex.ws.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.GlobalContext;
import ru.finex.core.component.ComponentService;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.ClusteredUidModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.ManagementModule;
import ru.finex.core.inject.module.NetworkModule;
import ru.finex.core.inject.module.PersistenceModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.utils.GenericUtils;
import ru.finex.ws.service.impl.ComponentServiceImpl;
import ru.finex.ws.tick.TickService;
import ru.finex.ws.tick.impl.RegisterTickListener;
import ru.finex.ws.tick.impl.TickServiceImpl;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode
public class WorldServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DbModule());
        install(new HoconModule());
        install(new PlaceholderJuelModule());
        install(new ClusterModule());
        install(new ClusteredUidModule());
        install(new PersistenceModule());
        install(new PoolModule());
        install(new NetworkModule());
        install(new CommandModule());
        install(new ManagementModule());
        bindPrototypeMappers();
        bind(ComponentService.class).to(ComponentServiceImpl.class);
        bind(TickService.class).to(TickServiceImpl.class);
        bindListener(Matchers.any(), new RegisterTickListener());
    }

    private void bindPrototypeMappers() {
        var mapperBinder = MapBinder.newMapBinder(
            binder(),
            new TypeLiteral<Class<? extends ComponentPrototype>>() { },
            new TypeLiteral<ComponentPrototypeMapper>() { },
            Names.named("ComponentMappers")
        );

        GlobalContext.reflections.getSubTypesOf(ComponentPrototypeMapper.class)
            .stream()
            .map(type -> Pair.of(
                GenericUtils.<ComponentPrototype>getInterfaceGenericType(type, ComponentPrototypeMapper.class, 0),
                type
            )).forEach(pair -> mapperBinder.addBinding(pair.getLeft()).to(pair.getRight()));
    }

}
