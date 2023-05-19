package ru.finex.core.math.bv.impl;

import java.util.function.Function;
import lombok.NonNull;
import ru.finex.core.math.ExtMath;
import ru.finex.core.math.Plane;
import ru.finex.core.math.Ray2f;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume2;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.VectorUtils;
import ru.finex.core.math.vector.alloc.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * Object-oriented bounding box implementation in 2-dimensional space.
 * <p>Follow the formula:</p>
 * <pre>{ C + ∑[i=0..1; x(i)*A(i)] : |x(i)| <= |a(i)| for all i }</pre>
 *
 * @author oracle
 */
public class OrientedBox2f implements BoundingVolume2 {

    /**
     * Center point of oriented box in global space.
     * In local space center of obb at (0, 0).
     * */
    private Vector2f center;

    /**
     * X-axis of the box. Must be normalized.
     * */
    private Vector2f unitAxisX;

    /**
     * Y-axis of the box (x rotated 90 degrees, counter-clockwise).
     * */
    private Vector2f unitAxisY;

    /**
     * Half-dimensions of the box measured along the X & Y axes.
     * */
    private Vector2f extents;

    public OrientedBox2f(@NonNull VectorAllocator<Vector2f> allocator) {
        this.center = allocator.alloc().set(Vector2f.ZERO);
        this.unitAxisX = allocator.alloc().set(Vector2f.UNIT_X);
        this.unitAxisY = allocator.alloc().set(Vector2f.UNIT_Y);
        this.extents = allocator.alloc().set(1f, 1f);
    }

    public OrientedBox2f(float centerX, float centerY, float extentX, float extentY, float angleRad,
                         VectorAllocator<Vector2f> allocator) {
        this.center = allocator.alloc().set(centerX, centerY);
        this.unitAxisX = allocator.alloc().set(ExtMath.cos(angleRad), ExtMath.sin(angleRad));
        this.unitAxisY = allocator.alloc().set(ExtMath.sin(angleRad), ExtMath.cos(angleRad));
        this.extents = allocator.alloc().set(extentX, extentY);
    }

    public OrientedBox2f(Vector2f center, Vector2f unitAxisX, Vector2f extents, VectorAllocator<Vector2f> allocator) {
        this(center.getX(), center.getY(), extents.getX(), extents.getY(), Vector2f.UNIT_X.angle(unitAxisX), allocator);
    }

    public OrientedBox2f(Vector2f center, float angleRad, Vector2f extents, VectorAllocator<Vector2f> allocator) {
        this(center.getX(), center.getY(), extents.getX(), extents.getY(), angleRad, allocator);
    }

    public OrientedBox2f(OrientedBox2f box, VectorAllocator<Vector2f> allocator) {
        this(box.center.getX(), box.center.getY(), box.extents.getX(), box.extents.getY(),
            Vector2f.UNIT_X.angle(box.unitAxisX), allocator);
    }

    public OrientedBox2f(Box2f box, VectorAllocator<Vector2f> allocator) {
        this((box.xmin + box.xmax) * 0.5f, (box.ymin + box.ymax) * 0.5f,
            box.getWidth() / 2f, box.getHeight() / 2f, 0f, allocator);
    }

    /**
     * Rotate this box to the specified angle.
     * @param angleRad rotation angle (in radians).
     * */
    public void setAngleRad(float angleRad) {
        this.unitAxisX.set(ExtMath.cos(angleRad), ExtMath.sin(angleRad));
        this.unitAxisY.set(ExtMath.sin(angleRad), ExtMath.cos(angleRad));
    }

    /**
     * Transforms {@literal point} to the local space of the box.
     * Center at box center, axes aligned to box.
     * @param point a point that need to be converted to local space of this box.
     * @param allocator an allocator to be used for further vectors allocation.
     * @return a {@link Vector2f} of transformed point.
     * */
    public Vector2f toLocalSpace(Vector2f point, VectorAllocator<Vector2f> allocator) {
        Vector2f fromCenter = point.subtract(this.center, allocator.alloc());
        return fromCenter.set(getAxisX().dot(fromCenter), getAxisY().dot(fromCenter));
    }

    /**
     * Transforms 3-dimensional {@literal point} to the local 2-dimensional space of the box.
     * Center at box center, axes aligned to box.
     * @param point a point that need to be converted to local space of this box.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * @return a {@link Vector2f} of transformed point.
     * */
    public Vector2f toLocalSpace(Vector3f point, VectorAllocator<Vector2f> allocator) {
        Vector2f fromCenter = VectorUtils.downcast(point, allocator) - this.center;
        return fromCenter.set(getAxisX().dot(fromCenter), getAxisY().dot(fromCenter));
    }

    @Override
    public boolean contains(float x, float y) {
        return Math.abs(x) <= this.extents.getX() && Math.abs(y) <= this.extents.getY();
    }

    @Override
    public boolean contains(Vector2f point, VectorAllocator<Vector2f> allocator) {
        Vector2f localPoint = toLocalSpace(point, allocator);
        boolean result = contains(localPoint.getX(), localPoint.getY());

        allocator.free(localPoint);
        return result;
    }

    @Override
    public boolean contains(Vector3f point, VectorAllocator<Vector2f> allocator) {
        Vector2f localPoint = toLocalSpace(point, allocator);
        boolean result = contains(localPoint.getX(), localPoint.getY());

        allocator.free(localPoint);
        return result;
    }

    @Override
    public void move(Vector2f point) {
        this.center.plus(point);
    }

    @Override
    public void moveCenter(Vector2f point) {
        moveCenter(point.getX(), point.getY());
    }

    @Override
    public void moveCenter(float x, float y) {
        this.center.set(x, y);
    }

    @Override
    public void moveCenter(Vector3f point) {
        moveCenter(point.getX(), point.getZ());
    }

    @Override
    public void encapsulate(Vector2f point, VectorAllocator<Vector2f> allocator) {
        encapsulatePoints(v -> point, 1, allocator);
    }

    @Override
    public <T extends BoundingVolume<Vector2f>> void union(T boundingVolume, VectorAllocator<Vector2f> allocator) {
        if (boundingVolume instanceof OrientedBox2f box) {
            unionOrientedBox2(box, allocator);
        } else {
            throw new UnsupportedOperationException("Union operation of BV [" +
                boundingVolume.getClass().getSimpleName() "] is not implemented");
        }
    }

    /**
     * Expand obb to encapsulate specified box.
     * @param box a box that need to be joined.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * */
    private void unionOrientedBox2(OrientedBox2f box, VectorAllocator<Vector2f> allocator) {
        encapsulatePoints(idx -> box.getVertexAtIndex(idx, allocator), 4, allocator);
    }

    /**
     * Expand this obb to encapsulate extracted points.
     *
     * @param pointExtractor a function providing {@link Vector2f} of points for encapsulation.
     *  Function argument is {@link Integer} signifying vertex index.
     * @param pointsCount count of points to be extracted.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * */
    private void encapsulatePoints(Function<Integer, Vector2f> pointExtractor, int pointsCount,
                                   VectorAllocator<Vector2f> allocator) {
        float[] minBounds = {-this.extents.getX(), -this.extents.getY()};
        float[] maxBounds = {this.extents.getX(), this.extents.getY()};

        for (int pointId = 0; pointId < pointsCount; pointId++) {
            Vector2f point = pointExtractor.apply(pointId);
            Vector2f localPoint = toLocalSpace(point, allocator);

            for (int i = 0; i < 2; i++) {
                minBounds[i] = Math.min(minBounds[i], localPoint.getComponents()[i]);
                maxBounds[i] = Math.max(maxBounds[i], localPoint.getComponents()[i]);
            }

            allocator.free(localPoint);
        }

        // obb extents
        float bex = 0.5f * (maxBounds[0] - minBounds[0]);
        float bey = 0.5f * (maxBounds[1] - minBounds[1]);
        this.extents.set(bex, bey);

        // obb center offset
        float bcx = 0.5f * (minBounds[0] + maxBounds[0]);
        float bcy = 0.5f * (minBounds[1] + maxBounds[1]);
        this.center.set(bcx, bcy);
    }

    @Override
    public boolean intersects(BoundingVolume2 boundingVolume, VectorAllocator<Vector2f> allocator) {
        if (boundingVolume instanceof Box2f box) {
            float cx = (box.xmax + box.xmin) * 0.5f;
            float cy = (box.ymax + box.ymin) * 0.5f;

            Vector2f center = allocator.alloc().set(cx, cy);
            boolean result = intersectsAxisAlignedBox2(box.xmin, box.xmax, box.ymin, box.ymax, center, allocator);

            allocator.free(center);
            return result;
        } else if (boundingVolume instanceof OrientedBox2f box) {
            return intersectsOrientedBox2(box, allocator);
        } else if (boundingVolume instanceof Circle2f circle) {
            Vector2f center = toLocalSpace(circle.getCenter(), allocator);
            boolean result = intersectsCircle(center.getX(), center.getY(), circle.getRadius());

            allocator.free(center);
            return result;
        } else if (boundingVolume instanceof Ray2f ray) {
            return intersectsRay(ray, allocator);
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector2f> allocator2f,
                              VectorAllocator<Vector3f> allocator3f) {
        if (boundingVolume instanceof Box3f box) {
            float cx = (box.xmax + box.xmin) * 0.5f;
            float cy = (box.zmax + box.zmin) * 0.5f;

            Vector2f center = allocator2f.alloc().set(cx, cy);
            boolean result = intersectsAxisAlignedBox2(box.xmin, box.xmax, box.zmin, box.zmax, center, allocator2f);

            allocator2f.free(center);
            return result;
        } else if (boundingVolume instanceof OrientedBox3f box) {
            return box.intersects(box, allocator3f);
        } else if (boundingVolume instanceof Sphere3f sphere) {
            Vector2f center = toLocalSpace(sphere.getCenter(), allocator2f);
            boolean result = intersectsCircle(center.getX(), center.getY(), sphere.getRadius());

            allocator2f.free(center);
            return result;
        } else if (boundingVolume instanceof Plane plane) {
            return intersectsPlane(plane, allocator2f);
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    /**
     * Tests intersection with 2D AABB.
     *
     * @param xmin lhs x-axis point.
     * @param xmax rhs x-axis point.
     * @param ymin lhs y-axis point.
     * @param ymax rhs y-axis point.
     * @param center central point of aabb.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with aabb, otherwise {@literal false}.
     * */
    private boolean intersectsAxisAlignedBox2(float xmin, float xmax, float ymin, float ymax,
                                              Vector2f center, VectorAllocator<Vector2f> allocator) {
        Vector2f distance = this.center.subtract(center, allocator.alloc());
        Vector2f aabbExtents = allocator.alloc().set(xmax - xmin, ymax - ymin);

        boolean result = intersectsBox2(this.extents, aabbExtents, this.unitAxisX,
            this.unitAxisY, Vector2f.UNIT_X, Vector2f.UNIT_Y, distance);

        allocator.free(distance);
        allocator.free(aabbExtents);

        return result;
    }

    /**
     * Tests intersection with 2D OBB.
     *
     * @param box another oriented box to intersection test.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with another obb, otherwise {@literal false}.
     * */
    private boolean intersectsOrientedBox2(OrientedBox2f box, VectorAllocator<Vector2f> allocator) {
        Vector2f distance = this.center.subtract(box.center, allocator.alloc());

        boolean result = intersectsBox2(this.extents, box.extents, this.unitAxisX,
            this.unitAxisY, box.unitAxisX, box.unitAxisY, distance);

        allocator.free(distance);
        return result;
    }

    /**
     * Tests intersection with 3D OBB.
     *
     * @param box another oriented box to intersection test.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with another obb, otherwise {@literal false}.
     * */
    private boolean intersectsOrientedBox3(OrientedBox3f box, VectorAllocator<Vector2f> allocator) {
        Vector2f center = allocator.alloc().set(box.getCenter().getX(), box.getCenter().getZ());
        Vector2f axisX = allocator.alloc().set(box.getAxisX().getX(), box.getAxisX().getZ());
        Vector2f axisY = allocator.alloc().set(box.getAxisZ().getX(), box.getAxisZ().getZ());

        Vector2f extents = allocator.alloc().set(box.getWidth() * 0.5f, box.getDepth() * 0.5f);
        Vector2f distance = -(center - this.center);

        boolean result = intersectsBox2(this.extents, extents, this.unitAxisX,
            this.unitAxisY, axisX, axisY, distance);

        allocator.free(center);
        allocator.free(axisX);
        allocator.free(axisY);
        allocator.free(distance);
        return result;
    }

    /**
     * Tests intersection between two bounding boxes, takes only their states.
     *
     * @param e1 extents of first box
     * @param e2 extents of second box
     * @param ax1 x-axis of first box
     * @param ay1 y-axis of first box
     * @param ax2 x-axis of second box
     * @param ay2 y-axis of second box
     * @param distance distance between centers of two boxes
     * @return {@literal true} if this obb intersects with another bounding box, otherwise {@literal false}.
     * */
    private boolean intersectsBox2(Vector2f e1, Vector2f e2, Vector2f ax1, Vector2f ay1,
                                   Vector2f ax2, Vector2f ay2, Vector2f distance) {
        float dxx = Math.abs(ax1.dot(ax2));
        float dxy = Math.abs(ax1.dot(ay2));

        float rSum = e1.getX() + e2.getX() * dxx + e2.getY() * dxy;
        if (Math.abs(ax1.dot(distance)) > rSum) {
            return false;
        }

        float dyx = Math.abs(ay1.dot(ax2));
        float dyy = Math.abs(ay1.dot(ay2));

        rSum = e1.getY() + e2.getX() * dyx + e2.getY() * dyy;
        if (Math.abs(ay1.dot(distance)) > rSum) {
            return false;
        }

        rSum = e2.getX() + e1.getX() * dxx + e1.getY() * dyx;
        if (Math.abs(ax2.dot(distance)) > rSum) {
            return false;
        }

        rSum = e2.getY() + e1.getX() * dxy + e1.getY() * dyy;
        if (Math.abs(ay2.dot(distance)) > rSum) {
            return false;
        }

        return true;
    }

    /**
     * Tests intersection of obb with a circle.
     *
     * @param x x-axis coordinate of circle center point.
     * @param y y-axis coordinate of circle center point.
     * @param radius circle radius.
     * @return {@literal true} if this obb intersects with circle, otherwise {@literal false}.
     * */
    private boolean intersectsCircle(float x, float y, float radius) {
        final float sqRadius = radius * radius;

        float dxLhs = -this.extents.getX() - x;
        float dxRhs = this.extents.getX() - x;
        float dyLhs = -this.extents.getY() - y;
        float dyRhs = this.extents.getY() - y;

        float dxLhsSq = dxLhs * dxLhs;
        float dxRhsSq = dxRhs * dxRhs;
        float dyLhsSq = dyLhs * dyLhs;
        float dyRhsSq = dyRhs * dyRhs;

        return dxLhsSq + dyLhsSq < sqRadius || dxLhsSq + dyRhsSq < sqRadius ||
            dxRhsSq + dyLhsSq < sqRadius || dxRhsSq + dyRhsSq < sqRadius;
    }

    /**
     * Tests intersection of obb with a 2D Ray.
     * @param ray a ray to be tested.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with a ray, otherwise {@literal false}.
     * */
    private boolean intersectsRay(Ray2f ray, VectorAllocator<Vector2f> allocator) {
        Vector2f rayStart = -(toLocalSpace(ray.getStart(), allocator) - this.center);
        Vector2f rayDirection = toLocalSpace(ray.getDirection(), allocator);

        // ray-specific test
        boolean besideBox = rayStart.getX() * rayDirection.getX() >= 0 && rayStart.getY() * rayDirection.getY() >= 0 &&
            Math.abs(rayStart.getX()) > this.extents.getX() && Math.abs(rayStart.getY()) > this.extents.getY();

        if (besideBox) {
            // line-specific tests
            float lhs = Math.abs(rayDirection.dot(rayStart.perpendicularLocal()));
            float rhs = this.extents.getX() * Math.abs(rayDirection.getY()) +
                this.extents.getY() * Math.abs(rayDirection.getX());

            allocator.free(rayStart);
            allocator.free(rayDirection);

            return lhs <= rhs;
        }

        allocator.free(rayStart);
        allocator.free(rayDirection);

        return false;
    }

    /**
     * Tests intersection of obb with a 3D plane in 2D space.
     * @param plane a plane to be tested.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with a plane, otherwise {@literal false}.
     * */
    private boolean intersectsPlane(Plane plane, VectorAllocator<Vector2f> allocator) {
        Vector2f planeNormal = toLocalSpace(plane.getNormal(), allocator);

        float unsignedDistance = Math.abs(planeNormal.dot(this.center) - plane.getD());
        float radius = Math.abs(this.extents.getX() * planeNormal.getX()) +
            Math.abs(this.extents.getY() * planeNormal.getY());

        allocator.free(planeNormal);

        return unsignedDistance <= radius;
    }

    /**
     * Get vertex of this obb at specified index.
     * <p>Vertexes mapping:</p>
     * <pre>{@code
     *        +y
     *
     *       3---2
     * -x    |   |    +x
     *       0---1
     *
     *        -y
     * }</pre>
     * @param vertexIndex index of desired vertex, {@code vertexIndex ∈ [0; 3]}.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * @return {@link Vector2f} of vertex point.
     * */
    public Vector2f getVertexAtIndex(int vertexIndex, VectorAllocator<Vector2f> allocator) {
        if (vertexIndex < 0 || vertexIndex > 3) {
            throw new IllegalArgumentException("Vertex index condition failed; vertexIndex ∈ [0; 3]");
        }

        // vertexes [0: (-x, -y), 1: (x, -y), 2: (x, y), 3: (-x, y)]

        // positive at [1 2]
        float dx = (vertexIndex == 1 | vertexIndex == 2) ? this.extents.getX() : -this.extents.getX();

        // positive at [2 3]
        float dy = (vertexIndex == 2 | vertexIndex == 3) ? this.extents.getY() : -this.extents.getY();

        Vector2f result = allocator.alloc().set(this.center);

        // points at xy axes
        Vector2f pxa = allocator.alloc().set(unitAxisX) * dx;
        Vector2f pya = allocator.alloc().set(unitAxisY) * dy;

        result = result + pxa + pya;

        allocator.free(pxa);
        allocator.free(pya);

        return result;
    }

    public Vector2f getExtents() {
        return this.extents;
    }

    /**
     * Get obb width (size along x-axis).
     * @return width.
     * */
    public float getWidth() {
        return this.extents.getX() * 2f;
    }

    /**
     * Get obb height (size along y-axis).
     * @return height.
     * */
    public float getHeight() {
        return this.extents.getY() * 2f;
    }

    /**
     * Get obb unit x-axis.
     * @return {@link Vector2f} of unit x-axis.
     * */
    public Vector2f getAxisX() {
        return unitAxisX;
    }

    /**
     * Get obb unit y-axis.
     * @return {@link Vector2f} of unit y-axis.
     * */
    public Vector2f getAxisY() {
        return this.unitAxisY;
    }

    /**
     * Get obb center.
     * @return {@link Vector2f} of obb center.
     * */
    public Vector2f getCenter() {
        return this.center;
    }

    /**
     * Clone this obb state to a new obb instance using {@link VectorAllocators#defaultVector2f()} allocator.
     * @return new instance of {@link OrientedBox2f} with the same state.
     * */
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public OrientedBox2f clone() {
        return clone(VectorAllocators.defaultVector2f());
    }

    /**
     * Clone this obb state to a new obb instance using specified {@literal allocator}.
     * @return new instance of {@link OrientedBox2f} with the same state.
     * */
    public OrientedBox2f clone(VectorAllocator<Vector2f> allocator) {
        return new OrientedBox2f(this, allocator);
    }

    @Override
    public void dispose() {
        this.center = null;
        this.unitAxisX = null;
        this.unitAxisY = null;
        this.extents = null;
    }

}
