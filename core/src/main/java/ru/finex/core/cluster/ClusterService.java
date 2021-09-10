package ru.finex.core.cluster;

import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author m0nster.mind
 */
public interface ClusterService {

    HazelcastInstance getHazelcast();

    String getRole();
    String getRole(Member member);

}
