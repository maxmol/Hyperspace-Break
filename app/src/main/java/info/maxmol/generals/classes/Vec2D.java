package info.maxmol.generals.classes;

// My own Point class. With some useful additions.
public class Vec2D {
    public double x, y;

    public static final Vec2D zero = new Vec2D(0, 0);

    public Vec2D() {
        x = 0;
        y = 0;
    }

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vec2D v) {
        x += v.x;
        y += v.y;
    }

    public void sub(Vec2D v) {
        x -= v.x;
        y -= v.y;
    }

    public void multiply(Vec2D v) {
        x *= v.x;
        y *= v.y;
    }

    public void multiply(Double m) {
        x *= m;
        y *= m;
    }

    public Vec2D plus(Vec2D v) {
        return new Vec2D(x + v.x, y + v.y);
    }

    public Vec2D minus(Vec2D v) {
        return new Vec2D(x - v.x, y - v.y);
    }

    public Vec2D mul(Vec2D v) {
        return new Vec2D(x * v.x, y * v.y);
    }

    public Vec2D mul(Double n) {
        return new Vec2D(x * n, y * n);
    }

    public Vec2D copy() {
        return new Vec2D(x, y);
    }

    public static Vec2D random() {
        double degree = Math.toRadians(Math.random() * 360);
        return new Vec2D(Math.sin(degree), Math.cos(degree));
    }

    public static Vec2D Clamp(Vec2D vec2d, double min_x, double max_x, double min_y, double max_y) {
        return new Vec2D(MUtil.Clamp(vec2d.x, min_x, max_x), MUtil.Clamp(vec2d.y, min_y, max_y));
    }

    public double Distance(Vec2D v) {
        return Math.abs(this.x - v.x) + Math.abs(this.y - v.y);
    }

    public double Length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2D GetNormalized() {
        Vec2D v = new Vec2D();
        double len = Length();

        v.x = x/len;
        v.y = y/len;

        return v;
    }

    public void Rotate(double degrees) {
        Vec2D normal = this.GetNormalized();
        double ang = Math.toRadians(degrees);
        double sin = Math.sin(ang);
        double cos = Math.cos(ang);
        double newX = this.x * cos - this.y * sin;
        double newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;
    }

    public Vec2D GetRotated(double degrees) {
        Vec2D normal = this.GetNormalized();
        double ang = Math.toRadians(degrees);
        double sin = Math.sin(ang);
        double cos = Math.cos(ang);
        double newX = this.x * cos - this.y * sin;
        double newY = this.x * sin + this.y * cos;

        return new Vec2D(newX, newY);
    }

    public double getRotationTo(Vec2D targetVec) {
        double theta = Math.atan2(targetVec.y - this.y, targetVec.x - this.x);

        theta -= Math.PI/2.0;
        double angle = Math.toDegrees(theta);

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
