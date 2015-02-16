package application;

import javafx.scene.paint.Color;

public class Predator extends Boid {
    private double maxSpeed = 3;

    public Predator(double radius, Color color) {
        super(radius, color);
        setMaxSpeed(maxSpeed);
    }
}
