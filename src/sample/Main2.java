package learn.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sondre.Boid;

import java.util.ArrayList;
import java.util.Random;

public class Main2 extends Application {
    double RADIUS = 5;
    Color COLOR = Color.GREEN;
    public static Circle circle;
    static ArrayList<Circle> boids = new ArrayList<Circle>();
    public static Pane canvas;

    @Override
    public void start(final Stage primaryStage) {

        canvas = new Pane();
        final Scene scene = new Scene(canvas, 800, 600);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        circle = new Circle(15, Color.BLUE);
        circle.relocate(100, 100);

        Random random = new Random();
        for (int i = 0; i < 1; i++) {
            //double xSpeed = random.nextDouble()*8 - 4;
            //double ySpeed = random.nextDouble()*8 - 4;
            double xSpeed = 4;
            double ySpeed = 0;
            Circle boid = new Circle(RADIUS, COLOR);
            boid.relocate(400, 300);
            //boid.setVelocity(new Point2D(xSpeed, ySpeed));
            boids.add(boid);
            canvas.getChildren().add(boid);
        }


        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {

            double deltaX = 3;
            double deltaY = 3;

            @Override
            public void handle(final ActionEvent t) {
                for (Circle currentBoid : boids) {
                    currentBoid.setLayoutX(currentBoid.getLayoutX() + deltaX);
                    currentBoid.setLayoutY(currentBoid.getLayoutY() + deltaY);

                    final Bounds bounds = canvas.getBoundsInLocal();
                    final boolean atRightBorder = currentBoid.getLayoutX() >= (bounds.getMaxX() - currentBoid.getRadius());
                    final boolean atLeftBorder = currentBoid.getLayoutX() <= (bounds.getMinX() + currentBoid.getRadius());
                    final boolean atBottomBorder = currentBoid.getLayoutY() >= (bounds.getMaxY() - currentBoid.getRadius());
                    final boolean atTopBorder = currentBoid.getLayoutY() <= (bounds.getMinY() + currentBoid.getRadius());

                    if (atRightBorder || atLeftBorder) {
                        deltaX *= -1;
                    }
                    if (atBottomBorder || atTopBorder) {
                        deltaY *= -1;
                    }
                }

            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}