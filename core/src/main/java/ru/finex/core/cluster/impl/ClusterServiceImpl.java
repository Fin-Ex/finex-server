package ru.finex.core.cluster.impl;

import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.ServerRole;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ClusterServiceImpl implements ClusterService {

    @Getter
    private final HazelcastInstance hazelcast;

    @Override
    public String getRole() {
        return hazelcast.getCluster().getLocalMember().getAttribute(ServerRole.ATTRIBUTE_NAME);
    }

    @Override
    public String getRole(Member member) {
        return member.getAttribute(ServerRole.ATTRIBUTE_NAME);
    }

}
