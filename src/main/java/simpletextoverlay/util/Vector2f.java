package simpletextoverlay.util;

public class Vector2f {

    private float x;
    private float y;

    public Vector2f() {
        this(0, 0);
    }

    public Vector2f(final Vector2f vec) {
        this(vec.x, vec.y);
    }

    public Vector2f(final float num) {
        this(num, num);
    }

    public Vector2f(final float x, final float y) {
        this.setX(x);
        this.setY(y);
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final void setX(final float x) {
        this.x = x;
    }

    public final void setY(final float y) {
        this.y = y;
    }

    public Vector2f set(final Vector2f vec) {
        return set(vec.x, vec.y);
    }

    public Vector2f set(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.getX(), this.getY());
    }

}
