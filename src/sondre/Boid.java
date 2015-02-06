package sondre;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import math.Vector;
import sample.Main;

public class Boid extends Circle {
    Vector position = new Vector();
    Vector velocity = new Vector();

    public Boid(double radius, Color color) {
        super(radius, color);
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

    public Vector steer(Vector target) {
        Vector desired = Vector.subtract(this.getPosition(), target);
        if (desired.magnitude() > 0) {
            desired.limit(Main.MAX_SPEED);
            return desired.subtract(this.getVelocity());
        }
        return new Vector();

    }
}
