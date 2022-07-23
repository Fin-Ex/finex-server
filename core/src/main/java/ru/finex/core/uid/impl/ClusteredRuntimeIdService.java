package ru.finex.core.uid.impl;

import org.redisson.api.RBitSet;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.uid.RuntimeIdService;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClusteredRuntimeIdService implements RuntimeIdService {

    private static final long MAX_BITS = 4_294_967_295L; // 0xffff_ffff
    private final ClusterService clusterService;
    private final RBitSet bitset;
    private long position;

    @Inject
    public ClusteredRuntimeIdService(ClusterService clusterService) {
        this.clusterService = clusterService;
        bitset = clusterService.getClient().getBitSet(clusterService.getName(getClass()));
    }

    @Override
    public int generateId() {
        bitset.set(position);
        long nextPosition = nextClearBit(position);
        if (nextPosition == -1) {
            nextPosition = nextClearBit(0);
        }

        if (nextPosition == -1) {
            throw new RuntimeException("BitSet is overflow!");
        }

        int id = (int) position; // unsigned int to signed int
        position = nextPosition;

        return id;
    }

    @Override
    public void free(int id) {
        bitset.clear(Integer.toUnsignedLong(id));
    }

    private long nextClearBit(long position) {
        for (long offset = position; offset < MAX_BITS; offset++) {
            if (!bitset.get(offset)) {
                return offset;
            }
        }

        return -1;
    }

    @PreDestroy
    private void destroy() {
        if (clusterService.getInstances() == 1) {
            bitset.delete();
        }
    }
}