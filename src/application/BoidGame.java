package application;

import gui.GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import math.Vector;

import java.util.ArrayList;
import java.util.Random;

public class BoidGame extends Application {
    public static double BOID_RADIUS = 5;
    public static double PREDATOR_RADIUS = 7;
    public static double SEPARATION_WEIGHT = 5;
    public static double ALIGNMENT_WEIGHT = 4;
    public static double AVOIDANCE_WEIGHT = 2;
    public static double COHESION_WEIGHT = 1.5;
    public static double MAX_SPEED = 2;
    public static double MAX_FORCE = 0.05;

    public static ArrayList<Boid> boids = new ArrayList<Boid>();
    public static ArrayList<Circle> obstacles = new ArrayList<Circle>();
    public static ArrayList<Predator> predators = new ArrayList<Predator>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(GUI.initGUI(), 800, 600);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        Random random = new Random();
        for (int i = 0; i < 150; i++) {
            double xSpeed = random.nextDouble()*MAX_SPEED*2 - MAX_SPEED;
            double ySpeed = random.nextDouble()*MAX_SPEED*2 - MAX_SPEED;
            Boid boid = new Boid(BOID_RADIUS, Color.GREEN);
            boid.getPosition().setX(random.nextDouble() * 400);
            boid.getPosition().setY(random.nextDouble() * 300);
            boid.getVelocity().setX(xSpeed);
            boid.getVelocity().setY(ySpeed);
            GUI.getCanvas().getChildren().add(boid);
            boids.add(boid);
        }

        Timeline loop = new Timeline(new KeyFrame(Duration.millis(40), new EventHandler<ActionEvent>() {
            final Bounds bounds = GUI.getCanvas().getBoundsInLocal();
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        for (Boid currentBoid : boids) {
                            Vector separation = new Vector();
                            Vector alignment = new Vector();
                            Vector cohesion = new Vector();
                            int separation_count = 0;
                            int alignment_count = 0;
                            int cohesion_count = 0;
                            for (Boid otherBoid : boids) {
                                if (otherBoid.equals(currentBoid)) continue;
                                double distance = currentBoid.getPosition().distance(otherBoid.getPosition());
                                if (distance <= currentBoid.getDesiredSeparationRadius()) {
                                    separation.add(
                                            Vector.subtract(currentBoid.getPosition(), otherBoid.getPosition())
                                                    .normalize()
                                                    .divide(distance)
                                    );
                                    separation_count++;
                                }
                                if (distance < currentBoid.getNeighbourRadius()) {
                                    alignment.add(otherBoid.getVelocity());
                                    cohesion.add(otherBoid.getPosition());
                                    alignment_count++;
                                    cohesion_count++;
                                }

                            }
                            double closestObstacleDistance = Integer.MAX_VALUE;
                            Vector avoidance = new Vector();
                            for (Circle obstacle : obstacles) {
                                Vector obstaclePosition = new Vector(obstacle.getLayoutX(), obstacle.getLayoutY());
                                double distance = currentBoid.getPosition().distance(obstaclePosition);
                                if (distance < currentBoid.getObstacleAvoidanceRadius()) {
                                    Vector collisionVector = new Vector(currentBoid.getVelocity()).normalize().multiply(distance);
                                    if (collisionVector.add(currentBoid.getPosition()).distance(obstaclePosition) <= obstacle.getRadius() + currentBoid.getRadius()) {
                                        if (distance < closestObstacleDistance) {
                                            closestObstacleDistance = distance;
                                            avoidance = new Vector(collisionVector);
                                            avoidance.subtract(obstaclePosition).normalize();
                                        }
                                    }
                                }
                            }
                            if (separation_count > 0) {
                                separation.divide(separation_count);
                            }
                            if (alignment_count > 0) {
                                alignment.divide(alignment_count).limit(MAX_FORCE);
                            }
                            if (cohesion_count > 0) {
                                cohesion.divide(cohesion_count);
                                cohesion = currentBoid.steer(cohesion);
                            }

                            separation.multiply(SEPARATION_WEIGHT);
                            alignment.multiply(ALIGNMENT_WEIGHT);
                            cohesion.multiply(COHESION_WEIGHT);
                            avoidance.multiply(AVOIDANCE_WEIGHT);

                            boolean outsideRightEdge = currentBoid.getPosition().getX() >= bounds.getMaxX();
                            boolean outsideLeftEdge = currentBoid.getPosition().getX() <= bounds.getMinX();
                            boolean outsideTopEdge = currentBoid.getPosition().getY() >= bounds.getMaxY();
                            boolean outsideBottomEdge = currentBoid.getPosition().getY() <= bounds.getMinY();

                            if (outsideRightEdge) {
                                currentBoid.getPosition().setX(0);
                            } else if (outsideLeftEdge) {
                                currentBoid.getPosition().setX(bounds.getMaxX());
                            } else if (outsideTopEdge) {
                                currentBoid.getPosition().setY(0);
                            } else if (outsideBottomEdge) {
                                currentBoid.getPosition().setY(bounds.getMaxY());
                            }
                            /* Updating position/layout based on velocity */
                            Vector changeInVelocity = Vector.add(separation, alignment).add(cohesion).add(avoidance);
                            double angle = currentBoid.getVelocity().angleInDegrees();
                            currentBoid.getRotationTransform().setAngle(angle);
                            currentBoid.setVelocity(currentBoid.getVelocity().add(changeInVelocity).limit(MAX_SPEED));
                            currentBoid.setPosition(currentBoid.getPosition().add(currentBoid.getVelocity()));
                            currentBoid.setLayoutX(currentBoid.getPosition().getX());
                            currentBoid.setLayoutY(currentBoid.getPosition().getY());
                        }
                    }
                })
        );
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ArrayList<Circle> getObstacles() {
        return obstacles;
    }

    public static void setObstacles(ArrayList<Circle> obstacles) {
        BoidGame.obstacles = obstacles;
    }

    public static ArrayList<Predator> getPredators() {
        return predators;
    }

    public static void setPredators(ArrayList<Predator> predators) {
        BoidGame.predators = predators;
    }
}
