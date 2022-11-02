package ru.finex.core.cluster;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.module.ClusteredUidModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;

/**
 * @author m0nster.mind
 */
public class ClusterModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new HoconModule());
        install(new PlaceholderJuelModule());
        install(new ru.finex.core.inject.module.ClusterModule());
        install(new ClusteredUidModule());
    }

}
