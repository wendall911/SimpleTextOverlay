package simpletextoverlay.util;

public class Vector3f extends Vector2f {

    private float z;

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(final Vector3f vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vector3f(final float num) {
        this(num, num, num);
    }

    public Vector3f(final float x, final float y, final float z) {
        super(x, y);
        this.setZ(z);
    }

    public final float getZ() {
        return this.z;
    }

    public final void setZ(final float z) {
        this.z = z;
    }

    public Vector3f set(final Vector3f vec) {
        return set(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vector3f set(final float x, final float y, final float z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s]", this.getX(), this.getY(), this.getZ());
    }

}
