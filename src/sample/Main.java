package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import math.Vector;
import sondre.Boid;
import sondre.BoidTriangle;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    public static double RADIUS = 5;
    public static double NEIGHBOUR_RADIUS = 35;
    public static double DESIRED_SEPARATION_RADIUS = 10;
    public static double SEPARATION_WEIGHT = 4;
    public static double ALIGNMENT_WEIGHT = 1;
    public static double COHESION_WEIGHT = 3;

    public static double MAX_SPEED = 3;
    public static double MAX_FORCE = 0.10;
    Color COLOR = Color.GREEN;
    static ArrayList<Boid> boids = new ArrayList<Boid>();
    //static ArrayList<BoidTriangle> boids = new ArrayList<BoidTriangle>();
    public static Pane canvas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        canvas = new GridPane();
        final Scene scene = new Scene(canvas);
        canvas.setStyle("-fx-background-color: black;");
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        Slider separationSlider = new Slider(0, 10, SEPARATION_WEIGHT);
        separationSlider.setShowTickLabels(true);
        separationSlider.setBlockIncrement(0.1);

        Slider alignmentSlider = new Slider(0, 10, ALIGNMENT_WEIGHT);
        alignmentSlider.setShowTickLabels(true);
        alignmentSlider.setBlockIncrement(0.1);

        Slider cohesionSlider = new Slider(0, 10, COHESION_WEIGHT);
        cohesionSlider.setShowTickLabels(true);
        cohesionSlider.setBlockIncrement(0.1);

        Random random = new Random();
        for (int i = 0; i < 156; i++) {
            double xSpeed = random.nextDouble()*8 - 4;
            double ySpeed = random.nextDouble()*8 - 4;
            Boid boid = new Boid(RADIUS, COLOR);
            //BoidTriangle boid = new BoidTriangle();
            boid.getPosition().setX(random.nextDouble() * 400);
            boid.getPosition().setY(random.nextDouble() * 300);
            //boid.getPosition().setX(0);
            //boid.getPosition().setY(0);
            //boid.getVelocity().setX(xSpeed);
            //boid.getVelocity().setY(ySpeed);
            canvas.getChildren().add(boid);
            boids.add(boid);
        }

        Timeline loop = new Timeline(new KeyFrame(Duration.millis(40), new EventHandler<ActionEvent>() {
            final Bounds bounds = canvas.getBoundsInLocal();
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
                                if (distance <= DESIRED_SEPARATION_RADIUS) {
                                    separation.add(
                                            Vector.subtract(currentBoid.getPosition(), otherBoid.getPosition())
                                                    .normalize()
                                                    .divide(distance)
                                    );
                                    separation_count++;
                                }
                                if (distance < NEIGHBOUR_RADIUS) {
                                    alignment.add(otherBoid.getVelocity());
                                    cohesion.add(otherBoid.getPosition());
                                    alignment_count++;
                                    cohesion_count++;
                                }

                            }
                            if (separation_count > 0) {
                                separation.divide(separation_count);
                            }
                            if (alignment_count > 0) {
                                alignment.divide(alignment_count);
                            }
                            if (cohesion_count > 0) {
                                cohesion.divide(cohesion_count);
                                cohesion = currentBoid.steer(cohesion.divide(cohesion_count));
                            }

                            separation.multiply(SEPARATION_WEIGHT);
                            alignment.multiply(ALIGNMENT_WEIGHT).limit(Main.MAX_FORCE);
                            cohesion.multiply(COHESION_WEIGHT).limit(Main.MAX_FORCE);
                            currentBoid.setVelocity(
                                    currentBoid.getVelocity()
                                            .add(separation)
                                            .add(alignment)
                                            .add(cohesion)
                                            .limit(MAX_SPEED));

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
}
