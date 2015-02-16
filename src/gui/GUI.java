package gui;

import application.BoidGame;
import application.Predator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class GUI {
    private static Pane canvas;

    public static Pane getCanvas() {
        return canvas;
    }

    public static GridPane initGUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(800, 600);

        canvas = new Pane();
        canvas.setPrefSize(800, 450);
        canvas.setStyle("-fx-background-color: black;");
        gridPane.add(canvas, 0, 0);

        GridPane controlDashboard = new GridPane();
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
        Slider separationSlider = new Slider(0, 10, BoidGame.SEPARATION_WEIGHT);
        separationSlider.setBlockIncrement(0.1);
        separationSlider.setStyle("-fx-padding: 10px");
        final Label separationValue = new Label(Double.toString(separationSlider.getValue()));
        sliderGrid.add(separationLabel, 0, 0);
        sliderGrid.add(separationSlider, 1, 0);
        sliderGrid.add(separationValue, 2, 0);

        separationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                BoidGame.SEPARATION_WEIGHT = newVal.doubleValue();
                separationValue.setText(String.format("%.1f", newVal));
            }
        });

        Label alignmentLabel = new Label("Alignment weight: ");
        alignmentLabel.setMinWidth(150);
        alignmentLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider alignmentSlider = new Slider(0, 10, BoidGame.ALIGNMENT_WEIGHT);
        alignmentSlider.setBlockIncrement(0.1);
        alignmentSlider.setStyle("-fx-padding: 10px");
        final Label alignmentValue = new Label(Double.toString(alignmentSlider.getValue()));
        sliderGrid.add(alignmentLabel, 0, 1);
        sliderGrid.add(alignmentSlider, 1, 1);
        sliderGrid.add(alignmentValue, 2, 1);

        alignmentSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                BoidGame.ALIGNMENT_WEIGHT = newVal.doubleValue();
                alignmentValue.setText(String.format("%.1f", newVal));
            }
        });

        Label cohesionLabel = new Label("Cohesion weight: ");
        cohesionLabel.setMinWidth(150);
        cohesionLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider cohesionSlider = new Slider(0, 10, BoidGame.COHESION_WEIGHT);
        cohesionSlider.setBlockIncrement(0.1);
        cohesionSlider.setStyle("-fx-padding: 10px");
        final Label cohesionValue = new Label(Double.toString(cohesionSlider.getValue()));
        sliderGrid.add(cohesionLabel, 0, 2);
        sliderGrid.add(cohesionSlider, 1, 2);
        sliderGrid.add(cohesionValue, 2, 2);

        cohesionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                BoidGame.COHESION_WEIGHT = newVal.doubleValue();
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
        addObstacleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Random random = new Random();
                Bounds bounds = canvas.getBoundsInLocal();
                Circle obstacle = new Circle(10, Color.WHITE);
                obstacle.setLayoutX(bounds.getMaxX()*random.nextDouble());
                obstacle.setLayoutY(bounds.getMaxY()*random.nextDouble());
                canvas.getChildren().add(obstacle);
                BoidGame.getObstacles().add(obstacle);
            }
        });
        Button removeObstaclesButton = new Button("Remove obstacles");
        removeObstaclesButton.setMinWidth(200);
        removeObstaclesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Circle> obstacles = BoidGame.getObstacles();
                if (obstacles != null && obstacles.size() > 0) {
                    for (Circle obstacle : obstacles) {
                        canvas.getChildren().remove(obstacle);
                    }
                    BoidGame.setObstacles(new ArrayList<Circle>());
                }
            }
        });
        buttonGrid.add(obstaclesLabel, 0, 0, 2, 1);
        buttonGrid.add(addObstacleButton, 0, 1);
        buttonGrid.add(removeObstaclesButton, 1, 1);

        Label predatorsLabel = new Label("Predators");
        predatorsLabel.setStyle("-fx-padding: 10px;");
        predatorsLabel.setMinWidth(400);
        predatorsLabel.setAlignment(Pos.CENTER);
        Button addPredatorButton = new Button("Add predator");
        addPredatorButton.setMinWidth(200);
        addPredatorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Random random = new Random();
                Bounds bounds = canvas.getBoundsInLocal();
                Predator predator = new Predator(7, Color.RED);
                predator.setLayoutX(bounds.getMaxX()*random.nextDouble());
                predator.setLayoutY(bounds.getMaxY()*random.nextDouble());
                canvas.getChildren().add(predator);
                BoidGame.getPredators().add(predator);
            }
        });
        Button removePredatorButton = new Button("Remove predators");
        removePredatorButton.setMinWidth(200);
        removePredatorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Predator> predators = BoidGame.getPredators();
                if (predators != null && predators.size() > 0) {
                    for (Predator predator : predators) {
                        canvas.getChildren().remove(predator);
                    }
                    BoidGame.setPredators(new ArrayList<Predator>());
                }
            }
        });
        buttonGrid.add(predatorsLabel, 2, 0, 2, 1);
        buttonGrid.add(addPredatorButton, 2, 1);
        buttonGrid.add(removePredatorButton, 3, 1);

        controlDashboard.add(buttonGrid, 0, 1);

        gridPane.add(controlDashboard, 0, 1);
        return gridPane;
    }
}
