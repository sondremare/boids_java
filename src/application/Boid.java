package application;

import gui.GUI;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import math.Vector;

import java.util.Random;

public class Boid extends Polygon {
    Vector position = new Vector();
    Vector velocity = new Vector();
    private Rotate rotationTransform;
    private double radius;
    private double neighbourRadius;
    private double obstacleAvoidanceRadius;
    private double desiredSeparationRadius;
    private double maxSpeed = 2;
    private double maxForce = 0.05;

    public Boid() {

    }

    public Boid(double radius, Color color) {
        this.radius = radius;
        this.neighbourRadius = radius * 10;
        this.obstacleAvoidanceRadius = radius * 6;
        this.desiredSeparationRadius = radius * 3;

        Double[] points = new Double[]{(-0.6 * radius), (-0.8 * radius), (0.0), (1.0 * radius), (0.6 * radius), (-0.8 * radius), (0.0), (-0.4 * radius)};
        this.getPoints().addAll(points);

        Random random = new Random();
        getPosition().setX(random.nextDouble() * GUI.getCanvas().getBoundsInParent().getMaxX());
        getPosition().setY(random.nextDouble() * GUI.getCanvas().getBoundsInParent().getMaxY());
        getVelocity().setX(random.nextDouble() * maxSpeed * 2 - maxSpeed);
        getVelocity().setY(random.nextDouble() * maxSpeed * 2 - maxSpeed);

        rotationTransform = new Rotate();
        getTransforms().add(rotationTransform);
        setFill(color);
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMaxForce() {
        return maxForce;
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
            desired.multiply(maxSpeed);
            return desired.subtract(this.getVelocity()).limit(maxForce);
        }
        return new Vector();
    }
}
