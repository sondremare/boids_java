package sondre;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import math.Vector;
import sample.Main;

public class BoidTriangle extends Polygon {
    Vector position = new Vector();
    Vector velocity = new Vector();
    //Double[] points = new Double[]{0.0, 0.0, 4.0, 4.0, 8.0, 0.0, 4.0, 20.0};
    //Double[] points = new Double[]{8.0, 16.0, 8.0, 8.0, 0.0, 0.0, 8.0, 32.0, 16.0, 0.0, 8.0, 16.0};
    Double[] points = new Double[]{0.0, 0.0, 4.0, 16.0, 8.0, 0.0, 4.0, 4.0};
    Color color = Color.GREEN;

    public BoidTriangle() {
        super();
        setFill(color);
        this.getPoints().addAll(points);
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    /** The graphical layout is based on the top-left of the shape, therefor the actual center is 8px further up and
     * 4px further right
     * @return
     */
    public double getGraphicalPositionX() {
        return this.getPosition().getX() - 4;
    }

    public double getGraphicalPositionY() {
        return this.getPosition().getY() - 8;
    }

    public Vector steer(Vector target) {
        Vector desired = Vector.subtract(this.getPosition(), target);
        if (desired.magnitude() > 0) {
            desired.limit(Main.MAX_SPEED);
            return desired.subtract(this.getVelocity());
        }
        return new Vector();

    }
}
