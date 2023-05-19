package ru.finex.core.math.bv.impl;

import java.util.function.Function;
import lombok.NonNull;
import ru.finex.core.math.ExtMath;
import ru.finex.core.math.Plane;
import ru.finex.core.math.Ray3f;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume2;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.VectorUtils;
import ru.finex.core.math.vector.alloc.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * Object-oriented bounding box implementation in 3-dimensional space.
 * <p>Follow the formula:</p>
 * <pre>{ C + ∑[i=0..2; x(i)*A(i)] : |x(i)| <= |a(i)| for all i }</pre>
 *
 * TODO: quaternion-based realisation of obb rotation to minimize memory consumption
 * @author oracle
 */
public class OrientedBox3f implements BoundingVolume3 {

    /**
     * Center of oriented box in global space.
     * In arrears of local space center of obb at (0, 0, 0).
     * */
    private Vector3f center;

    /**
     * X-axis of the box. Must be normalized.
     * */
    private Vector3f unitAxisX;

    /**
     * Y-axis of the box (x rotated 90 degrees, counter-clockwise).
     * */
    private Vector3f unitAxisY;

    /**
     * Z-axis of the box.
     * */
    private Vector3f unitAxisZ;

    /**
     * Half-dimensions of the box measured along the X & Y & Z axes.
     * */
    private Vector3f extents;

    public OrientedBox3f(@NonNull VectorAllocator<Vector3f> allocator) {
        this.center = allocator.alloc().set(Vector3f.ZERO);
        this.unitAxisX = allocator.alloc().set(Vector3f.UNIT_X);
        this.unitAxisY = allocator.alloc().set(Vector3f.UNIT_Y);
        this.unitAxisZ = allocator.alloc().set(Vector3f.UNIT_Z);
        this.extents = allocator.alloc().set(1f, 1f, 1f);
    }

    public OrientedBox3f(float centerX, float centerY, float centerZ, float extentX, float extentY, float extentZ,
                         float angleRad, @NonNull VectorAllocator<Vector3f> allocator) {
        this.center = allocator.alloc().set(centerX, centerY, centerZ);
        this.unitAxisX = allocator.alloc().set(ExtMath.cos(angleRad), ExtMath.sin(angleRad), ExtMath.sin(angleRad));
        this.unitAxisY = allocator.alloc().set(ExtMath.sin(angleRad), ExtMath.cos(angleRad), ExtMath.sin(angleRad));
        this.unitAxisZ = allocator.alloc().set(ExtMath.sin(angleRad), ExtMath.sin(angleRad), ExtMath.cos(angleRad));
        this.extents = allocator.alloc().set(extentX, extentY, extentZ);
    }

    public OrientedBox3f(@NonNull Vector3f center, @NonNull Vector3f unitAxisX, @NonNull Vector3f extents,
                         @NonNull VectorAllocator<Vector3f> allocator) {
        this(center.getX(), center.getY(), center.getZ(), extents.getX(), extents.getY(), extents.getZ(),
            Vector3f.UNIT_X.angle(unitAxisX), allocator);
    }

    public OrientedBox3f(@NonNull Vector3f center, @NonNull Vector3f extents, float angleRad,
                         @NonNull VectorAllocator<Vector3f> allocator) {
        this(center.getX(), center.getY(), center.getZ(), extents.getX(), extents.getY(), extents.getZ(),
            angleRad, allocator);
    }

    public OrientedBox3f(@NonNull OrientedBox3f box, @NonNull VectorAllocator<Vector3f> allocator) {
        this(box.center.getX(), box.center.getY(), box.center.getZ(), box.extents.getX(), box.extents.getY(),
            box.extents.getZ(), Vector3f.UNIT_X.angle(box.unitAxisX), allocator);
    }

    public OrientedBox3f(@NonNull Box3f box, @NonNull VectorAllocator<Vector3f> allocator) {
        this((box.xmin + box.xmax) * 0.5f, (box.ymin + box.ymax) * 0.5f, (box.zmin + box.zmax) * 0.5f,
            box.getWidth() / 2f, box.getHeight() / 2f, box.getDepth() / 2f, 0f, allocator);
    }

    /**
     * Rotate this box to the specified angle.
     * @param angleRad rotation angle (in radians).
     * */
    public void setAngleRad(float angleRad) {
        // todo oracle: not sure about that (also used in constructor)
        this.unitAxisX.set(ExtMath.cos(angleRad), ExtMath.sin(angleRad), ExtMath.sin(angleRad));
        this.unitAxisY.set(ExtMath.sin(angleRad), ExtMath.cos(angleRad), ExtMath.sin(angleRad));
        this.unitAxisZ.set(ExtMath.sin(angleRad), ExtMath.sin(angleRad), ExtMath.cos(angleRad));
    }

    /**
     * Transforms 2-dimensional {@literal point} to the local 3-dimensional space of the box.
     * Center at box center, axes aligned to box.
     * @param point a point that need to be converted to local space of this box.
     * @param allocator an allocator to be used for further vectors allocation.
     * @return a new {@link Vector3f} of transformed point.
     * */
    @NonNull
    public Vector3f toLocalSpace(@NonNull Vector2f point, @NonNull VectorAllocator<Vector3f> allocator) {
        Vector3f fromCenter = VectorUtils.upcast(point, allocator) - this.center;
        return fromCenter.set(getAxisX().dot(fromCenter), getAxisY().dot(fromCenter), getAxisZ().dot(fromCenter));
    }

    /**
     * Transforms {@literal point} to the local space of the box.
     * Center at box center, axes aligned to box.
     * Beware that freeing memory for vector is within the competence of caller side.
     * @param point a point that need to be converted to local space of this box.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * @return a new {@link Vector3f} of transformed point.
     * */
    @NonNull
    public Vector3f toLocalSpace(@NonNull Vector3f point, @NonNull VectorAllocator<Vector3f> allocator) {
        Vector3f fromCenter = point.subtract(this.center, allocator.alloc());
        return fromCenter.set(getAxisX().dot(fromCenter), getAxisY(allocator).dot(fromCenter), getAxisZ().dot(fromCenter));
    }

    @Override
    public boolean contains(float x, float y, float z) {
        return Math.abs(x) <= this.extents.getX() &&
            Math.abs(y) <= this.extents.getY() &&
            Math.abs(z) <= this.extents.getZ();
    }

    @Override
    public boolean contains(@NonNull Vector3f point, @NonNull VectorAllocator<Vector3f> allocator) {
        Vector3f localPoint = toLocalSpace(point, allocator);
        boolean result = contains(localPoint.getX(), localPoint.getY(), localPoint.getZ());

        // cleanup resources
        allocator.free(localPoint);

        return result;
    }

    @Override
    public void move(@NonNull Vector3f point) {
        this.center.plus(point);
    }

    @Override
    public void moveCenter(@NonNull Vector3f point) {
        moveCenter(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public void moveCenter(float x, float y, float z) {
        this.center.set(x, y, z);
    }

    @Override
    public void encapsulate(@NonNull Vector3f point, @NonNull VectorAllocator<Vector3f> allocator) {
        encapsulatePoints(v -> point, 1, allocator);
    }

    @Override
    public <T extends BoundingVolume<Vector3f>> void union(@NonNull T boundingVolume,
                                                           @NonNull VectorAllocator<Vector3f> allocator) {
        if (boundingVolume instanceof OrientedBox3f box) {
            unionOrientedBox3(box, allocator);
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
    private void unionOrientedBox3(OrientedBox3f box, VectorAllocator<Vector3f> allocator) {
        encapsulatePoints(idx -> box.getVertexAtIndex(idx, allocator), 7, allocator);
    }

    /**
     * Expand this obb to encapsulate extracted points.
     *
     * @param pointExtractor a function providing {@link Vector3f} of points for encapsulation.
     *  Function argument is {@link Integer} signifying vertex index.
     * @param pointsCount count of points to be extracted.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * */
    private void encapsulatePoints(Function<Integer, Vector3f> pointExtractor, int pointsCount,
                                   VectorAllocator<Vector3f> allocator) {
        float[] minBounds = {-this.extents.getX(), -this.extents.getY(), -this.extents.getZ()};
        float[] maxBounds = {this.extents.getX(), this.extents.getY(), this.extents.getZ()};

        for (int pointId = 0; pointId < pointsCount; pointId++) {
            Vector3f point = pointExtractor.apply(pointId);
            Vector3f localPoint = toLocalSpace(point, allocator);

            for (int i = 0; i < 3; i++) {
                minBounds[i] = Math.min(minBounds[i], localPoint.getComponents()[i]);
                maxBounds[i] = Math.max(maxBounds[i], localPoint.getComponents()[i]);
            }

            // cleanup resources
            allocator.free(point);
            allocator.free(localPoint);
        }

        // obb extents
        float bex = 0.5f * (maxBounds[0] - minBounds[0]);
        float bey = 0.5f * (maxBounds[1] - minBounds[1]);
        float bez = 0.5f * (maxBounds[2] - minBounds[2]);
        this.extents.set(bex, bey, bez);

        // obb center offset
        float bcx = 0.5f * (minBounds[0] + maxBounds[0]);
        float bcy = 0.5f * (minBounds[1] + maxBounds[1]);
        float bcz = 0.5f * (minBounds[2] + maxBounds[2]);
        this.center.set(bcx, bcy, bcz);
    }

    @Override
    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector3f> allocator) {
        if (boundingVolume instanceof Box3f box) {
            float cx = (box.xmax + box.xmin) * 0.5f;
            float cy = (box.ymax + box.ymin) * 0.5f;
            float cz = (box.zmax + box.zmin) * 0.5f;

            Vector3f center = allocator.alloc().set(cx, cy, cz);
            boolean result = intersectsAxisAlignedBox3(box.xmin, box.xmax, box.ymin, box.ymax, box.zmin, box.zmax,
                center, allocator);

            allocator.free(center);
            return result;
        } else if (boundingVolume instanceof OrientedBox3f box) {
            return intersectsOrientedBox3(box, allocator);
        } else if (boundingVolume instanceof Sphere3f sphere) {
            Vector3f center = toLocalSpace(sphere.getCenter(), allocator);
            boolean result = intersectsCircle(center.getX(), 0f, center.getY(), sphere.getRadius());

            allocator.free(center);
            return result;
        } else if (boundingVolume instanceof Ray3f ray) {
            return intersectsRay(ray, allocator);
        } else if (boundingVolume instanceof Plane plane) {
            return intersectsPlane(plane, allocator);
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    public boolean intersects(BoundingVolume2 boundingVolume, VectorAllocator<Vector3f> allocator3f) {
        if (boundingVolume instanceof Box2f box) {
            float cx = (box.xmax + box.xmin) * 0.5f;
            float cy = (box.ymax + box.ymin) * 0.5f;

            return intersectsAxisAlignedBox2(box.xmin, box.xmax, box.ymin, box.ymax, cx, cy, allocator3f);
        } else if (boundingVolume instanceof OrientedBox2f box) {
            return intersectsOrientedBox2(box, allocator3f);
        } else if (boundingVolume instanceof Circle2f circle) {
            Vector3f center = toLocalSpace(VectorUtils.upcast(circle.getCenter(), allocator3f), allocator3f);
            boolean result = intersectsCircle(center.getX(), 0f, center.getY(), circle.getRadius());

            allocator3f.free(center);
            return result;
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    /**
     * Tests intersection with 3D AABB.
     *
     * @param xmin lhs x-axis point.
     * @param xmax rhs x-axis point.
     * @param ymin lhs y-axis point.
     * @param ymax rhs y-axis point.
     * @param cx box center coordinate on x-axis.
     * @param cy box center coordinate on y-axis.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with aabb, otherwise {@literal false}.
     * */
    private boolean intersectsAxisAlignedBox2(float xmin, float xmax, float ymin, float ymax, float cx, float cy,
                                              VectorAllocator<Vector3f> allocator) {
        Vector3f center3f = allocator.alloc().set(cx, 0f, cy);
        Vector3f distance = this.center.subtract(center3f, allocator.alloc());
        Vector3f aabbExtents = allocator.alloc().set(xmax - xmin, ymax - ymin, 0f);

        boolean result = intersectsBox3(this.extents, aabbExtents, this.unitAxisX, this.unitAxisY, this.unitAxisZ,
            Vector3f.UNIT_X, Vector3f.UNIT_Y, Vector3f.UNIT_Z, distance);

        allocator.free(center3f);
        allocator.free(distance);
        allocator.free(aabbExtents);

        return result;
    }

    /**
     * Tests intersection with 3D AABB.
     *
     * @param xmin lhs x-axis point.
     * @param xmax rhs x-axis point.
     * @param ymin lhs y-axis point.
     * @param ymax rhs y-axis point.
     * @param zmin lhs z-axis point.
     * @param zmax rhs z-axis point.
     * @param center central point of aabb.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with aabb, otherwise {@literal false}.
     * */
    private boolean intersectsAxisAlignedBox3(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax,
                                              Vector3f center, VectorAllocator<Vector3f> allocator) {
        Vector3f distance = this.center.subtract(center, allocator.alloc());
        Vector3f aabbExtents = allocator.alloc().set(xmax - xmin, ymax - ymin, zmax - zmin);

        boolean result = intersectsBox3(this.extents, aabbExtents, this.unitAxisX, this.unitAxisY, this.unitAxisZ,
            Vector3f.UNIT_X, Vector3f.UNIT_Y, Vector3f.UNIT_Z, distance);

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
    private boolean intersectsOrientedBox2(OrientedBox2f box, VectorAllocator<Vector3f> allocator) {
        Vector3f center = allocator.alloc().set(box.getCenter().getX(), 0f, box.getCenter().getY());
        Vector3f axisX = allocator.alloc().set(box.getAxisX().getX(), 0f, box.getAxisX().getY());
        Vector3f axisZ = allocator.alloc().set(box.getAxisY().getX(), 0f, box.getAxisY().getY());

        Vector3f extents = allocator.alloc().set(box.getWidth() * 0.5f, 0f, box.getHeight() * 0.5f);
        Vector3f distance = -(center - this.center);

        boolean result = intersectsBox3(this.extents, extents, this.unitAxisX,
            this.unitAxisY, this.unitAxisZ, axisX, Vector3f.ZERO, axisZ, distance);

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
    private boolean intersectsOrientedBox3(OrientedBox3f box, VectorAllocator<Vector3f> allocator) {
        Vector3f distance = this.center.subtract(box.center, allocator.alloc());

        boolean result = intersectsBox3(this.extents, box.extents, this.unitAxisX,
            this.unitAxisY, this.unitAxisZ, box.unitAxisX, box.unitAxisY, box.unitAxisZ, distance);

        allocator.free(distance);
        return result;
    }

    /**
     * Tests intersection between two 3D bounding boxes, takes only their states.
     *
     * @param e1 extents of first box.
     * @param e2 extents of second box.
     * @param ax1 x-axis of first box.
     * @param ay1 y-axis of first box.
     * @param az1 z-axis of first box.
     * @param ax2 x-axis of second box.
     * @param ay2 y-axis of second box.
     * @param az2 z-axis of second box.
     * @param distance distance between centers of two boxes.
     * @return {@literal true} if this obb intersects with another bounding box, otherwise {@literal false}.
     * */
    private boolean intersectsBox3(Vector3f e1, Vector3f e2, Vector3f ax1, Vector3f ay1, Vector3f az1,
                                   Vector3f ax2, Vector3f ay2, Vector3f az2, Vector3f distance) {
        // TODO oracle: probably wrong algorithm for inters-test
        float dxx = Math.abs(ax1.dot(ax2));
        float dxy = Math.abs(ax1.dot(ay2));
        float dxz = Math.abs(ax1.dot(az2));

        float rSum = e1.getX() + e2.getX() * dxx + e2.getY() * dxy + e2.getZ() * dxz;
        if (Math.abs(ax1.dot(distance)) > rSum) {
            return false;
        }

        float dyx = Math.abs(ay1.dot(ax2));
        float dyy = Math.abs(ay1.dot(ay2));
        float dyz = Math.abs(ay1.dot(az2));

        rSum = e1.getY() + e2.getX() * dyx + e2.getY() * dyy + e2.getZ() * dyz;
        if (Math.abs(ay1.dot(distance)) > rSum) {
            return false;
        }

        float dzx = Math.abs(az1.dot(ax2));
        float dzy = Math.abs(az1.dot(ay2));
        float dzz = Math.abs(az1.dot(az2));

        rSum = e1.getZ() + e2.getX() * dzx + e2.getY() * dzy + e2.getZ() * dzz;
        if (Math.abs(az1.dot(distance)) > rSum) {
            return false;
        }

        rSum = e2.getX() + e1.getX() * dxx + e1.getY() * dyx + e1.getZ() * dzx;
        if (Math.abs(ax2.dot(distance)) > rSum) {
            return false;
        }

        rSum = e2.getY() + e1.getX() * dxy + e1.getY() * dyy + e1.getZ() * dzy;
        if (Math.abs(ay2.dot(distance)) > rSum) {
            return false;
        }

        rSum = e2.getZ() + e1.getX() * dxz + e1.getY() * dyz + e1.getZ() * dzz;
        if (Math.abs(az2.dot(distance)) > rSum) {
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
    private boolean intersectsCircle(float x, float y, float z, float radius) {
        final float sqRadius = radius * radius;

        float dxLhs = -this.extents.getX() - x;
        float dxRhs = this.extents.getX() - x;
        float dyLhs = -this.extents.getY() - y;
        float dyRhs = this.extents.getY() - y;
        float dzLhs = -this.extents.getZ() - z;
        float dzRhs = this.extents.getZ() - z;

        float dxLhsSq = dxLhs * dxLhs;
        float dxRhsSq = dxRhs * dxRhs;
        float dyLhsSq = dyLhs * dyLhs;
        float dyRhsSq = dyRhs * dyRhs;
        float dzLhsSq = dzLhs * dzLhs;
        float dzRhsSq = dzRhs * dzRhs;

        return dxLhsSq + dyLhsSq < sqRadius || dxLhsSq + dyRhsSq < sqRadius ||
            dxLhsSq + dzLhsSq < sqRadius || dxLhsSq + dzRhsSq < sqRadius ||
            dxRhsSq + dyLhsSq < sqRadius || dxRhsSq + dyRhsSq < sqRadius ||
            dxRhsSq + dzLhsSq < sqRadius || dxRhsSq + dzRhsSq < sqRadius;
    }

    /**
     * Tests intersection of obb with a 3D Ray.
     * @param ray a ray to be tested.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with a ray, otherwise {@literal false}.
     * */
    private boolean intersectsRay(Ray3f ray, VectorAllocator<Vector3f> allocator) {
        Vector3f rayStart = -(toLocalSpace(ray.getStart(), allocator) - this.center);
        Vector3f rayDirection = toLocalSpace(ray.getDirection(), allocator);

        // ray-specific test
        boolean besideBox = rayStart.getX() * rayDirection.getX() >= 0 && rayStart.getY() * rayDirection.getY() >= 0 &&
            rayStart.getZ() * rayDirection.getZ() >= 0 && Math.abs(rayStart.getX()) > this.extents.getX() &&
            Math.abs(rayStart.getY()) > this.extents.getY() && Math.abs(rayStart.getZ()) > this.extents.getZ();

        if (besideBox) {
            Vector3f dxs = rayDirection.cross(rayStart, allocator.alloc());

            boolean disjoint = Math.abs(dxs.getX()) > this.extents.getY() * Math.abs(rayDirection.getZ()) + this.extents.getZ() * Math.abs(rayDirection.getY()) ||
                Math.abs(dxs.getY()) > this.extents.getX() * Math.abs(rayDirection.getZ()) + this.extents.getZ() * Math.abs(rayDirection.getX()) ||
                Math.abs(dxs.getZ()) > this.extents.getX() * Math.abs(rayDirection.getY()) + this.extents.getY() * Math.abs(rayDirection.getX());

            // cleanup resources
            allocator.free(rayStart);
            allocator.free(rayDirection);
            allocator.free(dxs);

            return disjoint;
        }

        allocator.free(rayStart);
        allocator.free(rayDirection);

        return false;
    }

    /**
     * Tests intersection of obb with a 3D plane in 3D space.
     * @param plane a plane to be tested.
     * @param allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with a plane, otherwise {@literal false}.
     * */
    private boolean intersectsPlane(Plane plane, VectorAllocator<Vector3f> allocator) {
        Vector3f planeNormal = toLocalSpace(plane.getNormal(), allocator);

        float unsignedDistance = Math.abs(planeNormal.dot(this.center) - plane.getD());
        float radius = Math.abs(this.extents.getX() * planeNormal.getX()) +
            Math.abs(this.extents.getY() * planeNormal.getY()) +
            Math.abs(this.extents.getZ() * planeNormal.getZ());

        allocator.free(planeNormal);

        return unsignedDistance <= radius;
    }

    /**
     * Get vertex of this obb at specified index.
     * <p>Vertexes mapping:</p>
     * <pre>{@code
     *
     *        7---6  +z               3---2  -z
     *       /|  /|                  /|  /|
     *      / 4-/-5                 / 0-/-1
     *     3---2 /         or      7---6 /
     *     |   |/                  |   |/
     *     0---1     -z            4---5     +z
     *
     * }</pre>
     * @param vertexIndex index of desired vertex, {@code vertexIndex ∈ [0; 7]}.
     * @return {@link Vector3f} of vertex point.
     * */
    public Vector3f getVertexAtIndex(int vertexIndex, @NonNull VectorAllocator<Vector3f> allocator) {
        if (vertexIndex < 0 || vertexIndex > 7) {
            throw new IllegalArgumentException("Vertex index condition failed; vertexIndex ∈ [0; 7]");
        }

        // vertexes [0: (-x, -y, -z), 1: (x, -y, -z), 2: (x, y, -z), 3: (-x, y, -z),
        //           4: (-x, -y, z), 5: (x, -y, z), 6:(x, y, z), 7:(-x, y, z)]

        // positive at [1 2 5 6]
        float dx = ((vertexIndex & 1) != 0) ^ ((vertexIndex & 2) != 0) ? this.extents.getX() : -this.extents.getX();

        // positive at [2 3 6 7]
        float dy = ((vertexIndex / 2) % 2) == 0 ? this.extents.getY() : -this.extents.getY();

        // positive at [4 5 6 7]
        float dz = vertexIndex > 3 ? this.extents.getZ() : -this.extents.getZ();

        Vector3f result = allocator.alloc().set(this.center);

        // points at xyz axes
        Vector3f pxa = allocator.alloc().set(unitAxisX) * dx;
        Vector3f pya = allocator.alloc().set(unitAxisY) * dy;
        Vector3f pza = allocator.alloc().set(unitAxisZ) * dz;

        result = result + pxa + pya + pza;

        // cleanup resources
        allocator.free(pxa);
        allocator.free(pya);
        allocator.free(pza);

        return result;
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
     * Get obb depth (size along z-axis).
     * @return depth.
     * */
    public float getDepth() {
        return this.extents.getZ() * 2f;
    }

    /**
     * Get obb center.
     * @return read-only {@link Vector3f} of obb center.
     * */
    public Vector3f getCenter() {
        return this.center;
    }

    /**
     * Get obb unit x-axis.
     * @return {@link Vector3f} of unit x-axis.
     * */
    @NonNull
    public Vector3f getAxisX() {
        return this.unitAxisX;
    }

    /**
     * Get obb unit y-axis.
     * @return {@link Vector3f} of unit y-axis.
     * */
    @NonNull
    public Vector3f getAxisY() {
        return this.unitAxisY;
    }

    /**
     * Get obb unit z-axis.
     * @return {@link Vector3f} of unit z-axis.
     * */
    @NonNull
    public Vector3f getAxisZ() {
        return this.unitAxisZ;
    }

    /**
     * Clone this obb state to a new obb instance using {@link VectorAllocators#defaultVector3f()} ()} allocator.
     * @return new instance of {@link OrientedBox3f} with the same state.
     * */
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public OrientedBox3f clone() {
        return clone(VectorAllocators.defaultVector3f());
    }

    /**
     * Clone this obb state to a new obb instance using specified {@literal allocator}.
     * @return new instance of {@link OrientedBox3f} with the same state.
     * */
    public OrientedBox3f clone(VectorAllocator<Vector3f> allocator) {
        return new OrientedBox3f(this, allocator);
    }

    @Override
    public void dispose() {
        this.center = null;
        this.unitAxisX = null;
        this.unitAxisY = null;
        this.unitAxisZ = null;
        this.extents = null;
    }

}
