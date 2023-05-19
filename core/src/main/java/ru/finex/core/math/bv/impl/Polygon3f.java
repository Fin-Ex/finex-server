package ru.finex.core.math.bv.impl;

import java.util.Arrays;
import javax.annotation.Nullable;
import lombok.Getter;
import ru.finex.core.math.FloatVectorMath;
import ru.finex.core.math.Plane;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * Geometry 3D polygon.
 *
 * @author m0nster.mind
 * @since wgp 27.09.2018
 */
public class Polygon3f implements BoundingVolume3, Cloneable {

    private static final float EPSILON_ON_PLANE = 0.1f;
    private static final float EPSILON_CWW = 0.001f;
    private static final float EPSILON_SAME_POINTS = 0.002f;

    /**
     * The polygon vertices.
     */
    @Getter
    private final Vector3f[] vertices;

    /**
     * The polygon plane.
     */
    @Getter
    private final Plane plane;

    /**
     * Custom flags.
     */
    private int flags;

    private Box3f box;

    /**
     * Construct polygon from the vertices.
     *
     * @param vertices the vertices.
     * @param allocator vector allocator.
     * @throws IllegalArgumentException throw if vertices is less than 3
     * @see Polygon3f#Polygon3f(Vector3f, Vector3f, Vector3f, VectorAllocator)
     */
    public Polygon3f(Vector3f[] vertices, VectorAllocator<Vector3f> allocator) throws IllegalArgumentException {
        if (vertices.length < 3) {
            throw new IllegalArgumentException("Polygon cannot have less than 3 vertices.");
        }

        this.vertices = vertices;

        var normal = allocator.alloc();
        var normalLanes = normal.floatVector();
        for (int i = 2; i < vertices.length; i++) {
            var ab = vertices[i - 1].subtract(vertices[0]);
            var ac = vertices[i].subtract(vertices[0]);

            normalLanes = normalLanes.add(FloatVectorMath.cross128(ab, ac));
        }

        this.plane = new Plane(getPlanePoint(), normal.normalizeLocal());
    }

    /**
     * Construct polygon from the 3 vertices.
     *
     * @param first the first vertex.
     * @param second the second vertex.
     * @param third the third vertex.
     * @param allocator vector allocator.
     * @see Polygon3f#Polygon3f(Vector3f[], VectorAllocator)
     */
    public Polygon3f(Vector3f first, Vector3f second, Vector3f third, VectorAllocator<Vector3f> allocator) {
        this(new Vector3f[] {first, second, third}, allocator);
    }

    /**
     * Construct this polygon from other polygon.
     * @param polygon other polygon
     * @param allocator vector allocator
     */
    public Polygon3f(Polygon3f polygon, VectorAllocator<Vector3f> allocator) {
        this(polygon.vertices, allocator);
    }

    /**
     * Determines if that polygon has duplicate vertices.
     *
     * @return false if found duplicate vertex
     */
    public boolean isValid() {
        for (int i = 0; i < vertices.length; i++) {
            var a = vertices[i];

            for (int j = i + 1; j < vertices.length; j++) {
                var b = vertices[j];
                if (a.equals(b, EPSILON_SAME_POINTS)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Return plane point. It's a first polygon vertex.
     *
     * @return the plane point.
     */
    public Vector3f getPlanePoint() {
        return vertices[0];
    }

    /**
     * Return aabb with this polygon encapsulate (polygon AABB).
     * @return aabb
     */
    public Box3f getBox() {
        if (box == null) {
            box = new Box3f();
            for (Vector3f vertex : vertices) {
                box.encapsulate(vertex);
            }
        }

        return box;
    }

    /**
     * Return polygon custom flags. By default, it is zero.
     *
     * @return flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Set polygon custom flags.
     *
     * @param flags flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Toggle flag bit.
     *
     * @param flag flag
     * @return flag status
     */
    public boolean toggleFlag(int flag) {
        flags ^= flag;
        return isFlagSet(flag);
    }

    /**
     * Return flag status.
     *
     * @param flag flag
     * @return true if flag is set
     */
    public boolean isFlagSet(int flag) {
        return (flags & flag) != 0;
    }

    /**
     * Set flag bit.
     *
     * @param flag flag
     */
    public void setFlag(int flag) {
        flags |= flag;
    }

    /**
     * Unset flag bit.
     *
     * @param flag flag
     */
    public void unsetFlag(int flag) {
        flags &= ~flag;
    }

    /**
     * Return mid-point of this polygon.
     *
     * @param result result vector
     * @return mid-point from vector buffer
     */
    public Vector3f getMidPoint(Vector3f result) {
        var resultLanes = result.floatVector();

        for (Vector3f vertex : vertices) {
            resultLanes = resultLanes.add(vertex.floatVector());
        }

        return result.set(resultLanes.div(vertices.length));
    }

    /**
     * Test polygon vertices to coplanar.
     *
     * @return true if polygon vertices is coplanar
     */
    public boolean isCoplanar() {
        for (var vertice : vertices) {
            if (!isOnPlane(vertice)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if point on polygon plane.
     *
     * @param point point
     * @return true if point on plane
     */
    public boolean isOnPlane(Vector3f point) {
        float distance = plane.distance(point);
        return distance > -EPSILON_ON_PLANE && distance < EPSILON_ON_PLANE;
    }

    /**
     * Determines if line AB intersect polygon.<br/>
     * If point isn't null and line intersect polygon then point coordinates is set to intersection.
     *
     * @param startLine start line point
     * @param endLine end line point
     * @param point [out] point with intersection coordinates (non-null)
     * @return true if line AB intersect polygon
     */
    @SuppressWarnings("checkstyle:UnnecessaryParentheses")
    public boolean intersects(Vector3f startLine, Vector3f endLine, Vector3f point) {
        float aDistance = plane.distance(startLine, vertices[0]);
        float bDistance = plane.distance(endLine, vertices[0]);

        if ((aDistance < 0 && bDistance < 0) || (aDistance > 0 && bDistance > 0)) {
            return false;
        }

        plane.rayIntersection(startLine, endLine, vertices[0], point);
        if (point.equals(startLine, EPSILON_ON_PLANE) || point.equals(endLine, EPSILON_ON_PLANE)) {
            return false;
        }

        return contains(point);
    }

    @Override
    public boolean contains(float x, float y, float z) {
        Vector3f point = new Vector3f(x, y, z); // FIXME m0nster.mind: vector allocation
        return contains(point);
    }

    @Override
    public boolean contains(Vector3f point, @Nullable VectorAllocator<Vector3f> allocator) {
        return contains(point);
    }

    public boolean contains(Vector3f point) {
        int low = 0;
        int high = vertices.length;

        do {
            int mid = (low + high) / 2;

            if (isTriangleCCW(vertices[0], vertices[mid], point)) {
                low = mid;
            } else {
                high = mid;
            }

        } while (low + 1 < high);

        if (low == 0 || high == vertices.length) {
            return false;
        }

        return isTriangleCCW(vertices[low], vertices[high], point);
    }

    /**
     * Determines if triangle specified by 3 points is defined counterclockwise.
     *
     * @param first first vertex.
     * @param second second vertex.
     * @param third third vertex.
     * @return true if defined.
     */
    private boolean isTriangleCCW(Vector3f first, Vector3f second, Vector3f third) {
        var abAcCross = FloatVectorMath.cross128(
            first.subtract(second),
            first.subtract(third)
        );

        float dotProduct = FloatVectorMath.dot128(plane.getNormal().floatVector(), abAcCross);
        return dotProduct > EPSILON_CWW;
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector3f> allocator) {
        if (boundingVolume instanceof Box3f box) {
            Box3f boundingBox = getBox();
            if (!box.intersects(boundingBox)) {
                return false;
            }

            for (int i = 0; i < vertices.length; i++) {
                Vector3f vertex = vertices[i];
                Vector3f prevVertex = vertices[i == 0 ? vertices.length - 1 : i - 1];

                if (box.intersects(prevVertex, vertex)) {
                    return true;
                }
            }

        } else if (boundingVolume instanceof Sphere3f sphere) {
            Box3f boundingBox = getBox();
            if (!boundingBox.intersects(sphere, allocator)) {
                return false;
            }

            for (int i = 0; i < vertices.length; i++) {
                Vector3f vertex = vertices[i];
                Vector3f prevVertex = vertices[i == 0 ? vertices.length - 1 : i - 1];

                if (sphere.intersects(prevVertex, vertex)) {
                    return true;
                }
            }
        } else {
            throw new UnsupportedOperationException("Intersection test with shape [" +
                boundingVolume.getClass().getSimpleName() + "] is not implemented");
        }

        return false;
    }

    @Override
    public void moveCenter(Vector3f point) {
        // TODO oracle: implement this???
    }

    @Override
    public void moveCenter(float x, float y, float z) {
        // TODO oracle: implement this???
    }

    @Override
    public void move(Vector3f point) {
        // TODO oracle: implement this???
    }

    @Override
    public void encapsulate(Vector3f point, VectorAllocator<Vector3f> allocator) {
        // TODO oracle: implement this???
    }

    @Override
    public <T extends BoundingVolume<Vector3f>> void union(T boundingVolume, VectorAllocator<Vector3f> allocator) {
        // TODO oracle: implement this???
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Polygon(vertices=" + Arrays.toString(vertices) +
            ", plane=" + plane +
            ", flags=" + flags + ")";
    }

    @Override
    public Polygon3f clone() {
        return clone(VectorAllocators.defaultVector3f());
    }

    public Polygon3f clone(VectorAllocator<Vector3f> allocator) {
        return new Polygon3f(this, allocator);
    }

}
