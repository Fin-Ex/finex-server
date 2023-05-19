package ru.finex.core.math;

import lombok.Getter;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;

import static java.lang.Float.floatToIntBits;

/**
 * Geometry 3D plane.<br/>
 * Follow to the formula: <pre>Ax + By + Cz + D = 0</pre>
 *
 * @author m0nster.mind
 * @since wgp 25.09.2018
 */
public class Plane {

    /**
     * The plane normal.
     */
    @Getter
    private final Vector3f normal;

    /**
     * The D component, inverted by sign.
     */
    @Getter
    private float d;

    public Plane(Vector3f first, Vector3f second, Vector3f third, VectorAllocator<Vector3f> allocator) {
        var ba = second.subtract(first, allocator.alloc());
        var ca = third.subtract(first, allocator.alloc());

        normal = ba.crossLocal(ca).normalizeLocal();
        d = first.dot(normal);

        allocator.free(ca);
    }

    public Plane(Vector3f planePoint, Vector3f normal) {
        this.normal = normal.clone();
        this.d = planePoint.dot(normal);
    }

    /**
     * Add plane by other plane.
     *
     * @param plane the other plane.
     * @return this plane
     */
    public Plane addLocal(Plane plane) {
        normal.addLocal(plane.normal);
        d += plane.d;
        return this;
    }

    /**
     * Add values to plane.
     *
     * @param x the X axis value.
     * @param y the Y axis value.
     * @param z the Z axis value.
     * @param d the D component.
     * @return this plane .
     */
    public Plane addLocal(float x, float y, float z, float d) {
        normal.addLocal(x, y, z);
        this.d += d;
        return this;
    }

    /**
     * Add plane by vector.<br/>
     * Its operation is equals to: plane normal plus vector.
     *
     * @param vector the vector.
     * @return this plane.
     */
    public Plane addLocal(Vector3f vector) {
        normal.addLocal(vector);
        return this;
    }

    /**
     * Subtract plane by other plane.
     *
     * @param plane the other plane
     * @return this plane
     */
    public Plane subtractLocal(Plane plane) {
        normal.subtractLocal(plane.normal);
        d -= plane.d;
        return this;
    }

    /**
     * Subtract values to plane.
     *
     * @param x the X axis value.
     * @param y the Y axis value.
     * @param z the Z axis value.
     * @param d the D component.
     * @return this plane.
     */
    public Plane subtractLocal(float x, float y, float z, float d) {
        normal.subtractLocal(x, y, z);
        this.d -= d;
        return this;
    }

    /**
     * Subtract plane by vector.<br/>
     * Its operation is equals to: plane normal minus vector.
     *
     * @param vector the vector.
     * @return this plane.
     */
    public Plane subtractLocal(Vector3f vector) {
        normal.subtractLocal(vector);
        return this;
    }

    /**
     * Multiply plane by scalar.
     *
     * @param scalar scalar.
     * @return this plane.
     */
    public Plane multLocal(float scalar) {
        normal.multLocal(scalar);
        d *= scalar;
        return this;
    }

    /**
     * Multiply plane by other plane.
     *
     * @param plane the other plane.
     * @return this plane .
     */
    public Plane multLocal(Plane plane) {
        normal.multLocal(plane.normal);
        d *= plane.d;
        return this;
    }

    /**
     * Multiply plane by vector.<br/>
     * Its operation is equals to multiply plane normal vector with vector.
     *
     * @param vector the vector.
     * @return this plane.
     */
    public Plane multLocal(Vector3f vector) {
        normal.multLocal(vector);
        return this;
    }

    /**
     * Divide plane by scalar.
     *
     * @param scalar scalar.
     * @return this plane .
     */
    public Plane divideLocal(float scalar) {
        normal.divideLocal(scalar);
        d /= scalar;
        return this;
    }

    /**
     * Divide plane by other plane.
     *
     * @param plane the other plane.
     * @return this plane .
     */
    public Plane divideLocal(Plane plane) {
        normal.divideLocal(plane.normal);
        d /= plane.d;
        return this;
    }

    /**
     * Divide plane by vector.<br/>
     * Its operation is equals to divide plane normal vector with vector.
     *
     * @param vector the vector.
     * @return this plane .
     */
    public Plane divideLocal(Vector3f vector) {
        normal.divideLocal(vector);
        return this;
    }

    /**
     * Dot product plane with vector.
     *
     * @param point vector
     * @return dot product
     */
    public float dot(Vector3f point) {
        return normal.dot(point) - d;
    }

    /**
     * Dot product plane with plane.
     *
     * @param plane plane
     * @return dot product
     */
    public float dot(Plane plane) {
        return normal.dot(plane.normal) - d * plane.d;
    }

    /**
     * Distance between the point and the plane.
     *
     * @param point      the point.
     * @param planePoint the plane point.
     * @return the distance.
     */
    public float distance(Vector3f point, Vector3f planePoint) {
        return FloatVectorMath.dot128(
            point.floatVector().sub(planePoint.floatVector()),
            normal.floatVector()
        );
    }

    /**
     * Distance between point and plane.
     *
     * @param point point
     * @return distance
     */
    public float distance(Vector3f point) {
        return -d + point.dot(normal);
    }

    /**
     * Angle between planes.
     *
     * @param plane plane
     * @return angle in radians
     */
    public float angle(Plane plane) {
        return ExtMath.cos(normal.dot(plane.normal) /
            ExtMath.sqrt(normal.sqrLength() * plane.normal.sqrLength()));
    }

    /**
     * Return true if the planes are parallel.
     *
     * @param plane   the plane.
     * @param epsilon the epsilon.
     * @return true if the planes are parallel.
     */
    public boolean isParallel(Plane plane, float epsilon) {
        // check plane normals to collinearity
        float fA = plane.normal.getX() / normal.getX();
        float fB = plane.normal.getY() / normal.getY();
        float fC = plane.normal.getZ() / normal.getZ();

        return Math.abs(fA - fB) < epsilon && Math.abs(fA - fC) < epsilon;
    }

    /**
     * Return true if the planes are perpendicular.
     *
     * @param plane   the plane.
     * @param epsilon the epsilon.
     * @return true if the planes are perpendicular.
     */
    public boolean isPerpendicular(Plane plane, float epsilon) {
        return Math.abs(normal.dot(plane.normal)) < epsilon;
    }

    /**
     * Ray-plane intersection. Return point where ray intersect plane.<br/>
     * <i>This method doesn't check plane-vector collinearity!</i>
     *
     * @param startPoint the start point.
     * @param endPoint the end point.
     * @param result the result.
     * @return the intersection point (result).
     */
    public Vector3f rayIntersection(Vector3f startPoint, Vector3f endPoint, Vector3f result) {
        var delta = endPoint.subtract(startPoint);

        // calculate dot product
        result.set(delta.mul(normal.floatVector()));
        float denominator = result.getX() + result.getY() + result.getZ();
        float distance = (d - startPoint.dot(normal)) / denominator;

        var intersection = startPoint.floatVector()
            .add(delta.mul(distance));

        return result.set(intersection);
    }

    /**
     * Ray-plane intersection. Return point where ray intersect plane.<br/>
     * <i>This method doesn't check plane-vector collinearity!</i>
     *
     * @param startPoint the start point.
     * @param endPoint the end point.
     * @param planePoint plane point
     * @param result result vector
     * @return the intersection point (result).
     */
    public Vector3f rayIntersection(Vector3f startPoint, Vector3f endPoint, Vector3f planePoint, Vector3f result) {
        var delta = endPoint.subtract(startPoint);

        // calculate dot product
        result.set(delta.mul(normal.floatVector()));
        float denominator = result.getX() + result.getY() + result.getZ();

        planePoint.subtract(startPoint, result); // diff
        float distance = result.dot(normal) / denominator;

        var intersection = startPoint.floatVector()
            .add(delta.mul(distance));

        return result.set(intersection);
    }

    /**
     * Ray-plane intersection. Return point where ray intersect plane.<br/>
     * <i>This method doesn't check plane-vector collinearity!</i>
     *
     * @param ray the ray.
     * @param result the result vector.
     * @return the intersection point from vector buffer.
     */
    public Vector3f rayIntersection(Ray3f ray, Vector3f result) {
        var direction = ray.getDirection();
        var start = ray.getStart();

        float denominator = direction.dot(normal);
        float distance = (d - start.dot(normal)) / denominator;

        var intersection = start.floatVector()
            .add(direction.floatVector().mul(distance));

        return result.set(intersection);
    }

    /**
     * Line-plane (segment-plane) intersection. Return point where line intersect plane.<br/>
     * If line and plane is parallel or lines doesn't intersect plane return {@link Vector3f#POSITIVE_INFINITY} const.
     *
     * @param startPoint the line start point.
     * @param secondPoint the line end point.
     * @param result the result vector.
     * @return intersection point from vector buffer (result) or {@link Vector3f#POSITIVE_INFINITY}
     */
    public Vector3f lineIntersection(Vector3f startPoint, Vector3f secondPoint, Vector3f result) {
        secondPoint.subtract(startPoint, result); // ab

        float t = (d - normal.dot(startPoint)) / normal.dot(result);
        if (t < 0 || t > 1.f) {
            return Vector3f.POSITIVE_INFINITY;
        }

        var intersection = startPoint.floatVector()
            .add(result.floatVector().mul(t));

        return result.set(intersection);
    }

    /**
     * Plane-plane intersection. Return point where planes intersect.<br/>
     * If planes is parallel return {@link Vector3f#POSITIVE_INFINITY} const.
     *
     * @param plane the plane.
     * @param epsilon the epsilon.
     * @param result the result vector.
     * @return the intersection point from vector buffer (result) or {@link Vector3f#POSITIVE_INFINITY}.
     */
    public Vector3f planeIntersection(Plane plane, float epsilon, Vector3f result) {
        normal.cross(plane.normal, result);

        float denominator = result.dot(result);
        if (denominator < epsilon) {
            // these planes are parallel
            return Vector3f.POSITIVE_INFINITY;
        }

        var direction = result.floatVector();
        var delta = normal.floatVector().mul(plane.d);
        var intersection = plane.normal.floatVector()
            .mul(d)
            .sub(delta);

        intersection = FloatVectorMath.cross128(intersection, direction)
            .div(denominator);

        return result.set(intersection);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        var other = (Plane) obj;
        if (!normal.equals(other.normal)) {
            return false;
        }

        return floatToIntBits(d) == floatToIntBits(other.d);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + normal.hashCode();
        result = prime * result + Float.floatToIntBits(d);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Plane(normal=" + normal + ", d=" + d + ")";
    }

    /* Operator overriding */

    /**
     * Operator {@code +} for Plane.
     *
     * @param rhs right value
     * @return this
     * @see #addLocal(Plane)
     */
    public Plane plus(Plane rhs) {
        return addLocal(rhs);
    }

    /**
     * Operator {@code +} for Vector3f.
     *
     * @param rhs right value
     * @return this
     * @see #addLocal(Vector3f)
     */
    public Plane plus(Vector3f rhs) {
        return addLocal(rhs);
    }

    /**
     * Operator {@code -} for Plane.
     *
     * @param rhs right value
     * @return this
     * @see #subtractLocal(Plane)
     */
    public Plane minus(Plane rhs) {
        return subtractLocal(rhs);
    }

    /**
     * Operator {@code -} for Vector3f.
     *
     * @param rhs right value
     * @return this
     * @see #subtractLocal(Vector3f)
     */
    public Plane minus(Vector3f rhs) {
        return subtractLocal(rhs);
    }

    /**
     * Operator {@code *} for Vector2f.
     *
     * @param rhs right value
     * @return this
     * @see #multLocal(Plane)
     */
    public Plane times(Plane rhs) {
        return multLocal(rhs);
    }

    /**
     * Operator {@code *} for Vector3f.
     *
     * @param rhs right value
     * @return this
     * @see #multLocal(Vector3f)
     */
    public Plane times(Vector3f rhs) {
        return multLocal(rhs);
    }

    /**
     * Operator {@code *} for float.
     *
     * @param rhs right value
     * @return this
     * @see #multLocal(float)
     */
    public Plane times(float rhs) {
        return multLocal(rhs);
    }

    /**
     * Operator {@code /} for Plane.
     *
     * @param rhs right value
     * @return this
     * @see #divideLocal(Plane)
     */
    public Plane div(Plane rhs) {
        return divideLocal(rhs);
    }

    /**
     * Operator {@code /} for Vector3f.
     *
     * @param rhs right value
     * @return this
     * @see #divideLocal(Vector3f)
     */
    public Plane div(Vector3f rhs) {
        return divideLocal(rhs);
    }

    /**
     * Operator {@code /} for float.
     *
     * @param rhs right value
     * @return this
     * @see #divideLocal(float)
     */
    public Plane div(float rhs) {
        return divideLocal(rhs);
    }

}