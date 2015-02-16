package math;

public class Vector {
    private double x;
    private double y;

    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    public Vector (Vector v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector normalize() {
        double mag = this.magnitude();
        if (mag > 0) {
            this.divide(mag);
        }
        return this;
    }

    public Vector add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
        return this;
    }

    public static Vector add(Vector v, Vector w) {
        return new Vector(v.getX() + w.getX(), v.getY() + w.getY());
    }

    public Vector subtract(Vector v) {
        this.x -= v.getX();
        this.y -= v.getY();
        return this;
    }

    public static Vector subtract(Vector v, Vector w) {
        return new Vector(v.getX() - w.getX(), v.getY() - w.getY());
    }

    public Vector divide(double d) {
        this.x /= d;
        this.y /= d;
        return this;
    }

    public Vector multiply(double d) {
        this.x *= d;
        this.y *= d;
        return this;
    }

    public double distance(Vector v) {
        return Math.sqrt(Math.pow(this.x - v.getX(), 2) + Math.pow(this.y - v.getY(), 2));
    }

    public double angleInDegrees() {
        Vector v = new Vector(0,1);
        double angleRadians = Math.acos(this.dotProduct(v) / (this.magnitude() * v.magnitude()));
        if (this.getX() >= 0) angleRadians *= -1;
        return angleRadians * (180 / Math.PI);
    }

    public double dotProduct(Vector v) {
        return this.getX() * v.getX() + this.getY() * v.getY();
    }

    public Vector limit(double maxValue) {
        if (this.magnitude() > maxValue) {
            return this.normalize().multiply(maxValue);
        }
        return this;
    }

    @Override
    public Vector clone(){
        return new Vector(this.x, this.y);
    }
}
