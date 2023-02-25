package ru.finex.relay.service.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.model.event.RelayEvent;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.relay.service.TopicResolverService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Default topic resolver.
 * <p>Using {@link ClusterService#getName(Class) ClusterService#getName} to resolve topic.
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class TopicResolverServiceImpl implements TopicResolverService {

    private final ClusterService clusterService;

    @Override
    public String resolve(ClientSession session, RelayEvent dto) {
        return clusterService.getName(dto.getClass());
    }

}
