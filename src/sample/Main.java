package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import math.Vector;
import sondre.Boid;

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
    public static GridPane gridPane;
    public static GridPane controlDashboard;

    @Override
    public void start(Stage primaryStage) throws Exception{

        gridPane = new GridPane();
        gridPane.setPrefSize(800, 600);

        canvas = new Pane();
        canvas.setPrefSize(800, 450);
        canvas.setStyle("-fx-background-color: black;");
        gridPane.add(canvas, 0, 0);

        controlDashboard = new GridPane();
        controlDashboard.setPrefSize(800, 150);
        controlDashboard.setStyle("-fx-background-color: lightgrey;");

        GridPane sliderGrid = new GridPane();
        sliderGrid.setPrefSize(800, 100);

        sliderGrid.getColumnConstraints().add(new ColumnConstraints(150));
        sliderGrid.getColumnConstraints().add(new ColumnConstraints(500));
        sliderGrid.getColumnConstraints().add(new ColumnConstraints(150));

        Label separationLabel = new Label("Separation weight: ");
        separationLabel.setMinWidth(150);
        separationLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider separationSlider = new Slider(0, 10, SEPARATION_WEIGHT);
        separationSlider.setBlockIncrement(0.1);
        separationSlider.setStyle("-fx-padding: 10px");
        final Label separationValue = new Label(Double.toString(separationSlider.getValue()));
        sliderGrid.add(separationLabel, 0, 0);
        sliderGrid.add(separationSlider, 1, 0);
        sliderGrid.add(separationValue, 2, 0);

        separationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                SEPARATION_WEIGHT = newVal.doubleValue();
                separationValue.setText(String.format("%.1f", newVal));
            }
        });

        Label alignmentLabel = new Label("Alignment weight: ");
        alignmentLabel.setMinWidth(150);
        alignmentLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider alignmentSlider = new Slider(0, 10, ALIGNMENT_WEIGHT);
        alignmentSlider.setBlockIncrement(0.1);
        alignmentSlider.setStyle("-fx-padding: 10px");
        final Label alignmentValue = new Label(Double.toString(alignmentSlider.getValue()));
        sliderGrid.add(alignmentLabel, 0, 1);
        sliderGrid.add(alignmentSlider, 1, 1);
        sliderGrid.add(alignmentValue, 2, 1);

        alignmentSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                ALIGNMENT_WEIGHT = newVal.doubleValue();
                alignmentValue.setText(String.format("%.1f", newVal));
            }
        });

        Label cohesionLabel = new Label("Cohesion weight: ");
        cohesionLabel.setMinWidth(150);
        cohesionLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider cohesionSlider = new Slider(0, 10, COHESION_WEIGHT);
        cohesionSlider.setBlockIncrement(0.1);
        cohesionSlider.setStyle("-fx-padding: 10px");
        final Label cohesionValue = new Label(Double.toString(cohesionSlider.getValue()));
        sliderGrid.add(cohesionLabel, 0, 2);
        sliderGrid.add(cohesionSlider, 1, 2);
        sliderGrid.add(cohesionValue, 2, 2);

        cohesionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                COHESION_WEIGHT = newVal.doubleValue();
                cohesionValue.setText(String.format("%.1f", newVal));
            }
        });

        controlDashboard.add(sliderGrid, 0, 0);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setPrefSize(800, 50);

        buttonGrid.getColumnConstraints().add(new ColumnConstraints(200));
        buttonGrid.getColumnConstraints().add(new ColumnConstraints(200));
        buttonGrid.getColumnConstraints().add(new ColumnConstraints(200));
        buttonGrid.getColumnConstraints().add(new ColumnConstraints(200));

        Label obstaclesLabel = new Label("Obstacles");
        obstaclesLabel.setStyle("-fx-padding: 10px;");
        obstaclesLabel.setMinWidth(400);
        obstaclesLabel.setAlignment(Pos.CENTER);
        Button addObstacleButton = new Button("Add obstacle");
        addObstacleButton.setMinWidth(200);
        Button removeObstaclesButton = new Button("Remove obstacles");
        removeObstaclesButton.setMinWidth(200);
        buttonGrid.add(obstaclesLabel, 0, 0, 2, 1);
        buttonGrid.add(addObstacleButton, 0, 1);
        buttonGrid.add(removeObstaclesButton, 1, 1);

        Label predatorsLabel = new Label("Predators");
        predatorsLabel.setStyle("-fx-padding: 10px;");
        predatorsLabel.setMinWidth(400);
        predatorsLabel.setAlignment(Pos.CENTER);
        Button addPredatorButton = new Button("Add predator");
        addPredatorButton.setMinWidth(200);
        Button removePredatorButton = new Button("Remove predators");
        removePredatorButton.setMinWidth(200);
        buttonGrid.add(predatorsLabel, 2, 0, 2, 1);
        buttonGrid.add(addPredatorButton, 2, 1);
        buttonGrid.add(removePredatorButton, 3, 1);

        controlDashboard.add(buttonGrid, 0, 1);

        gridPane.add(controlDashboard, 0, 1);

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
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
