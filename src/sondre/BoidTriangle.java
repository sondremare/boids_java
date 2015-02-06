package sondre;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import math.Vector;

public class BoidTriangle extends Polygon {
    Vector position = new Vector();
    Vector velocity = new Vector();
    //Double[] points = new Double[]{0.0, 0.0, 4.0, 4.0, 8.0, 0.0, 4.0, 20.0};
    Double[] points = new Double[]{16.0, 16.0, 8.0, 8.0, 0.0, 0.0, 8.0, 40.0, 16.0, 0.0};
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
}
