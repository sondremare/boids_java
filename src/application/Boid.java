package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import math.Vector;

public class Boid extends Polygon {
    Vector position = new Vector();
    Vector velocity = new Vector();
    private Rotate rotationTransform;
    private double radius;
    private double neighbourRadius;
    private double obstacleAvoidanceRadius;
    private double desiredSeparationRadius;

    public Boid() {

    }

    public Boid(double radius, Color color) {
        this.radius = radius;
        this.neighbourRadius = radius * 10;
        this.obstacleAvoidanceRadius = radius * 6;
        this.desiredSeparationRadius = radius * 3;
        Double[] points = new Double[]{(-0.6 * radius), (-0.8 * radius), (0.0), (1.0 * radius), (0.6 * radius), (-0.8 * radius), (0.0), (-0.4 * radius)};
        this.getPoints().addAll(points);
        rotationTransform = new Rotate();
        setFill(color);
        getTransforms().add(rotationTransform);
    }

    public double getNeighbourRadius() {
        return neighbourRadius;
    }

    public double getObstacleAvoidanceRadius() {
        return obstacleAvoidanceRadius;
    }

    public double getDesiredSeparationRadius() {
        return desiredSeparationRadius;
    }

    public Rotate getRotationTransform() {
        return this.rotationTransform;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public double getRadius() {
        return radius;
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
        Vector desired = Vector.subtract(target, this.getPosition());
        if (desired.magnitude() > 0) {
            desired.normalize();
            desired.multiply(BoidGame.MAX_SPEED);
            return desired.subtract(this.getVelocity()).limit(BoidGame.MAX_FORCE);
        }
        return new Vector();
    }
}
