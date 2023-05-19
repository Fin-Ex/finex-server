package ru.finex.core.math.vector.alloc;

import lombok.experimental.UtilityClass;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.Vector4f;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class VectorAllocators {

    private static final ThreadLocal<CachedAllocator<Vector2f>> CA_V2 = ThreadLocal.withInitial(() ->
        new CachedAllocator<>(defaultVector2f())
    );

    private static final ThreadLocal<CachedAllocator<Vector3f>> CA_V3 = ThreadLocal.withInitial(() ->
        new CachedAllocator<>(defaultVector3f())
    );

    private static final ThreadLocal<CachedAllocator<Vector4f>> CA_V4 = ThreadLocal.withInitial(() ->
        new CachedAllocator<>(defaultVector4f())
    );

    /**
     * Allocates a new vector.
     * @return vector allocator
     */
    public static VectorAllocator<Vector2f> defaultVector2f() {
        return new DefaultVectorAllocator<>() {
            @Override
            public Vector2f alloc() {
                return new Vector2f();
            }
        };
    }

    /**
     * Allocates a new vector.
     * @return vector allocator
     */
    public static VectorAllocator<Vector3f> defaultVector3f() {
        return new DefaultVectorAllocator<>() {
            @Override
            public Vector3f alloc() {
                return new Vector3f();
            }
        };
    }

    /**
     * Allocates a new vector.
     * @return vector allocator
     */
    public static VectorAllocator<Vector4f> defaultVector4f() {
        return new DefaultVectorAllocator<>() {
            @Override
            public Vector4f alloc() {
                return new Vector4f();
            }
        };
    }

    /**
     * Thread local cached allocator of {@link Vector2f}.
     * @return vector
     */
    public static CachedAllocator<Vector2f> cachedLocalAllocatorVector2f() {
        return CA_V2.get();
    }

    /**
     * Thread local cached allocator of {@link Vector3f}.
     * @return vector
     */
    public static CachedAllocator<Vector3f> cachedLocalAllocatorVector3f() {
        return CA_V3.get();
    }

    /**
     * Thread local cached allocator of {@link Vector4f}.
     * @return vector
     */
    public static CachedAllocator<Vector4f> cachedLocalAllocatorVector4f() {
        return CA_V4.get();
    }

    /**
     * Cached allocator of {@link Vector2f}.
     * @return vector
     */
    public static CachedAllocator<Vector2f> cachedAllocatorVector2f() {
        return new CachedAllocator<>(defaultVector2f());
    }

    /**
     * Cached allocator of {@link Vector3f}.
     * @return vector
     */
    public static CachedAllocator<Vector3f> cachedAllocatorVector3f() {
        return new CachedAllocator<>(defaultVector3f());
    }

    /**
     * Cached allocator of {@link Vector4f}.
     * @return vector
     */
    public static CachedAllocator<Vector4f> cachedAllocatorVector4f() {
        return new CachedAllocator<>(defaultVector4f());
    }

}
