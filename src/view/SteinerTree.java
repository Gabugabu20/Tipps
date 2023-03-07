package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Edge;
import model.Game;
import model.Node;
import model.Section;

public class SteinerTree extends Application {
    private GraphicsContext gc;
    // GUI Elements
    // Labels
    private Label actualValueLabel;
    private Label valueLabel;
    private Label nodesLabel;
    private Label timerLabel;
    // Buttons
    private Button tippButton;

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Edge> solution;
    private ArrayList<Section> tippSections;

    // Timer
    private Timeline timeline;
    private long startTime;

    private long timeToAdd;

    private Edge tippEdge;

    public SteinerTree(Game game) {
        this.nodes = game.getNodes();
        this.edges = game.getEdges();
        this.solution = game.getSolution();
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(600, 600);

        gc = canvas.getGraphicsContext2D();
        // initializes all GUI elements like labels and buttons
        initializeGUIElements();

        Button halfTippButton = new Button("Halb Tipp");
        halfTippButton.setLayoutX(20);
        halfTippButton.setLayoutY(20);

        halfTippButton.setOnAction(event -> {
            timeToAdd += 30;
            tippSections = new ArrayList<>();
            ArrayList<Edge> wrongEdges = new ArrayList<>();

            for (Edge edge : edges) {
                if (edge.isSelected()) {
                    if (!solution.contains(edge)) {
                        for (int i = 0; i < 2; i++) {
                            if (!tippSections.contains(edge.getSections().get(i))) {
                                tippSections.add(edge.getSections().get(i));
                                wrongEdges.add(edge);
                            }
                        }
                    } else {
                        System.out.println("Wurde korrekt ausgewählt:");
                        System.out.println(edge.toString());
                        System.out.println("------");
                    }
                } else {
                    if (solution.contains(edge)) {
                        System.out.println("Muss ausgewählt sein:");
                        System.out.println(edge.toString());
                        System.out.println("------");
                        for (int i = 0; i < 2; i++) {
                            if (!tippSections.contains(edge.getSections().get(i))) {
                                tippSections.add(edge.getSections().get(i));
                                wrongEdges.add(edge);
                            }
                        }
                    }
                }
            }

            System.out.println("Sections:");
            for (Section section : tippSections) {
                System.out.println(section);
            }

            // NEW
            if (this.tippEdge != null) {
                Random random = new Random();
                int num = random.nextInt(0, wrongEdges.size());
                wrongEdges.get(num);
            }

            System.out.println("-----------------------------------------------------------------------");

            gc.clearRect(0, 0, 600, 600);

            // set edge color
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);

            Random random = new Random();
            int randomNumber = 0;

            if (tippSections.size() != 0) {
                randomNumber = random.nextInt(tippSections.size());
            }

            // draw edges
            for (Edge edge : edges) {
                int fromNodeIndex = edge.getNode1().getNumber() - 1;
                int toNodeIndex = edge.getNode2().getNumber() - 1;
                double fromX = nodes.get(fromNodeIndex).getX();
                double fromY = nodes.get(fromNodeIndex).getY();
                double toX = nodes.get(toNodeIndex).getX();
                double toY = nodes.get(toNodeIndex).getY();

                if (tippSections.size() != 0) {

                    if (edge.getSections().contains(tippSections.get(randomNumber))) {
                        gc.setStroke(Color.ORANGE);
                    } else {
                        gc.setStroke(Color.BLACK);
                    }

                } else {
                    redrawCanvas(gc);
                }

                // if (edge.isSolution()) {
                // gc.setStroke(Color.GREEN);
                // }
                gc.strokeLine(fromX, fromY, toX, toY);
            }

            // set node color
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            // draw nodes
            ArrayList<Integer> nodesToConnect = new ArrayList<>(Arrays.asList(1, 4, 8, 12, 13));
            for (int i = 0; i < nodes.size(); i++) {
                double x = nodes.get(i).getX();
                double y = nodes.get(i).getY();
                if (nodesToConnect.contains(nodes.get(i).getNumber())) {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(x - 15, y - 15, 30, 30);
                    gc.strokeOval(x - 15, y - 15, 30, 30);
                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                    gc.setFill(Color.BLUE);
                } else {
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x - 15, y - 15, 30, 30);
                    gc.strokeOval(x - 15, y - 15, 30, 30);
                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                    gc.setFill(Color.BLACK);
                }
            }

            // set value
            int totalValue = 0;
            for (Edge edge : edges) {
                if (edge.isSelected()) {
                    totalValue += edge.getValue();
                }
            }
            valueLabel.setText(Integer.toString(totalValue));
        });

        Button quarterTippButton = new Button("Viertel Tipp");
        quarterTippButton.setLayoutX(20);
        quarterTippButton.setLayoutY(60);

        quarterTippButton.setOnAction(event -> {
            timeToAdd += 60;
            tippSections = new ArrayList<>();

            for (Edge edge : edges) {
                if (edge.isSelected()) {
                    if (!solution.contains(edge)) {
                        for (int i = 0; i < 2; i++) {
                            if (!tippSections.contains(edge.getSections().get(i))) {
                                tippSections.add(edge.getSections().get(i));
                            }
                        }
                    } else {
                        System.out.println("Wurde korrekt ausgewählt:");
                        System.out.println(edge.toString());
                        System.out.println("------");
                    }
                } else {
                    if (solution.contains(edge)) {
                        System.out.println("Muss ausgewählt sein:");
                        System.out.println(edge.toString());
                        System.out.println("------");
                        for (int i = 0; i < 2; i++) {
                            if (!tippSections.contains(edge.getSections().get(i))) {
                                tippSections.add(edge.getSections().get(i));
                            }
                        }
                    }
                }
            }

            System.out.println("Sections:");
            for (Section section : tippSections) {
                System.out.println(section);
            }
            System.out.println("-----------------------------------------------------------------------");

            gc.clearRect(0, 0, 600, 600);

            // set edge color
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);

            // Random random = new Random();
            // int randomNumber = 0;

            // if (tippSections.size() != 0) {
            // randomNumber = random.nextInt(tippSections.size());
            // }

            // draw edges

            Section[] horizontal = { Section.LINKS, Section.RECHTS };
            Section[] vertikal = { Section.OBEN, Section.UNTEN };
            Random random = new Random();
            int randomHorizontal = random.nextInt(2);
            int randomVertikal = random.nextInt(2);

            for (Edge edge : edges) {
                int fromNodeIndex = edge.getNode1().getNumber() - 1;
                int toNodeIndex = edge.getNode2().getNumber() - 1;
                double fromX = nodes.get(fromNodeIndex).getX();
                double fromY = nodes.get(fromNodeIndex).getY();
                double toX = nodes.get(toNodeIndex).getX();
                double toY = nodes.get(toNodeIndex).getY();

                if (tippSections.size() != 0) {
                    switch (tippSections.size()) {
                        case 2:
                            if (edge.getSections().contains(tippSections.get(0))
                                    && edge.getSections().contains(tippSections.get(1))) {
                                gc.setStroke(Color.ORANGE);
                            } else {
                                gc.setStroke(Color.BLACK);
                            }
                            break;
                        case 3:

                            break;
                        case 4:
                            if (edge.getSections().contains(horizontal[randomHorizontal])
                                    && edge.getSections().contains(vertikal[randomVertikal])) {
                                gc.setStroke(Color.ORANGE);
                            } else {
                                gc.setStroke(Color.BLACK);
                            }
                            break;
                        default:
                            break;
                    }

                } else {
                    redrawCanvas(gc);
                }

                // if (edge.isSolution()) {
                // gc.setStroke(Color.GREEN);
                // }
                gc.strokeLine(fromX, fromY, toX, toY);
            }

            // set node color
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            // draw nodes
            ArrayList<Integer> nodesToConnect = new ArrayList<>(Arrays.asList(1, 4, 8, 12, 13));
            for (int i = 0; i < nodes.size(); i++) {
                double x = nodes.get(i).getX();
                double y = nodes.get(i).getY();
                if (nodesToConnect.contains(nodes.get(i).getNumber())) {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(x - 15, y - 15, 30, 30);
                    gc.strokeOval(x - 15, y - 15, 30, 30);
                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                    gc.setFill(Color.BLUE);
                } else {
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x - 15, y - 15, 30, 30);
                    gc.strokeOval(x - 15, y - 15, 30, 30);
                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                    gc.setFill(Color.BLACK);
                }
            }

            // set value
            int totalValue = 0;
            for (Edge edge : edges) {
                if (edge.isSelected()) {
                    totalValue += edge.getValue();
                }
            }
            valueLabel.setText(Integer.toString(totalValue));
        });

        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // click on edge
        canvas.setOnMouseClicked(event -> handleEdgeClick(event, primaryStage));

        // set edge color
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        // draw edges
        for (Edge edge : edges) {
            int fromNodeIndex = edge.getNode1().getNumber() - 1;
            int toNodeIndex = edge.getNode2().getNumber() - 1;
            double fromX = nodes.get(fromNodeIndex).getX();
            double fromY = nodes.get(fromNodeIndex).getY();
            double toX = nodes.get(toNodeIndex).getX();
            double toY = nodes.get(toNodeIndex).getY();

            if (edge.isSelected()) {
                gc.setStroke(Color.RED);
            } else {
                gc.setStroke(Color.BLACK);
            }

            // if (edge.isSolution()) {
            // gc.setStroke(Color.GREEN);
            // }

            gc.strokeLine(fromX, fromY, toX, toY);
        }

        // set node color
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        // draw nodes
        ArrayList<Integer> nodesToConnect = new ArrayList<>(Arrays.asList(1, 4, 8, 12, 13));
        for (int i = 0; i < nodes.size(); i++) {
            double x = nodes.get(i).getX();
            double y = nodes.get(i).getY();
            if (nodesToConnect.contains(nodes.get(i).getNumber())) {
                gc.setFill(Color.BLUE);
                gc.fillOval(x - 15, y - 15, 30, 30);
                gc.strokeOval(x - 15, y - 15, 30, 30);
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                gc.setFill(Color.BLUE);
            } else {
                gc.setFill(Color.BLACK);
                gc.fillOval(x - 15, y - 15, 30, 30);
                gc.strokeOval(x - 15, y - 15, 30, 30);
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                gc.setFill(Color.BLACK);
            }
        }

        // set value
        int totalValue = 0;
        for (Edge edge : edges) {
            if (edge.isSelected()) {
                totalValue += edge.getValue();
            }
        }
        valueLabel.setText(Integer.toString(totalValue));

        startTime = System.currentTimeMillis();
        timeline.play();

        Pane root = new Pane(canvas, quarterTippButton, halfTippButton, actualValueLabel, valueLabel, nodesLabel,
                timerLabel);
        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Steinerbaum");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkSolution(Edge edge) {
        if (solution.contains(edge)) {
            edge.setSolution(true);
        }
    }

    private void redrawCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, 600, 600);

        // set edge color
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        // draw edges
        for (Edge edge : edges) {
            int fromNodeIndex = edge.getNode1().getNumber() - 1;
            int toNodeIndex = edge.getNode2().getNumber() - 1;
            double fromX = nodes.get(fromNodeIndex).getX();
            double fromY = nodes.get(fromNodeIndex).getY();
            double toX = nodes.get(toNodeIndex).getX();
            double toY = nodes.get(toNodeIndex).getY();

            if (edge.isSelected()) {
                gc.setStroke(Color.RED);
            } else {
                gc.setStroke(Color.BLACK);
            }

            // if (edge.isSolution()) {
            // gc.setStroke(Color.GREEN);
            // }
            gc.strokeLine(fromX, fromY, toX, toY);
        }

        // set node color
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        // draw nodes
        ArrayList<Integer> nodesToConnect = new ArrayList<>(Arrays.asList(1, 4, 8, 12, 13));
        for (int i = 0; i < nodes.size(); i++) {
            double x = nodes.get(i).getX();
            double y = nodes.get(i).getY();
            if (nodesToConnect.contains(nodes.get(i).getNumber())) {
                gc.setFill(Color.BLUE);
                gc.fillOval(x - 15, y - 15, 30, 30);
                gc.strokeOval(x - 15, y - 15, 30, 30);
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                gc.setFill(Color.BLUE);
            } else {
                gc.setFill(Color.BLACK);
                gc.fillOval(x - 15, y - 15, 30, 30);
                gc.strokeOval(x - 15, y - 15, 30, 30);
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(Integer.toString(nodes.get(i).getNumber()), x - 5, y + 8);
                gc.setFill(Color.BLACK);
            }
        }
    }

    private boolean checkFinish(ArrayList<Edge> edges) {
        ArrayList<Edge> selectedEdges = new ArrayList<>();
        int nodesFound = 0;
        for (Edge edge : edges) {
            if (edge.isSelected()) {
                selectedEdges.add(edge);
            }
        }
        for (Edge edge : selectedEdges) {
            if (solution.contains(edge)) {
                nodesFound++;
            } else {
                nodesFound--;
            }
        }
        if (nodesFound == 8) {
            return true;
        }
        return false;
    }

    private void initializeGUIElements() {
        actualValueLabel = new Label("Aktueller Wert:");
        actualValueLabel.setLayoutX(150);
        actualValueLabel.setLayoutY(20);

        valueLabel = new Label();
        valueLabel.setLayoutX(250);
        valueLabel.setLayoutY(20);

        nodesLabel = new Label("Diese Knoten zusammen verbinden: 1, 4, 8, 12, 13");
        nodesLabel.setLayoutX(150);
        nodesLabel.setLayoutY(50);

        timerLabel = new Label();
        timerLabel.setLayoutX(200);
        timerLabel.setLayoutY(100);
    }

    private void handleEdgeClick(MouseEvent event, Stage primaryStage){
        double mouseX = event.getX();
            double mouseY = event.getY();

            // set edge color
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);

            // edges
            for (Edge edge : edges) {
                int fromNodeIndex = edge.getNode1().getNumber() - 1;
                int toNodeIndex = edge.getNode2().getNumber() - 1;
                double fromX = nodes.get(fromNodeIndex).getX();
                double fromY = nodes.get(fromNodeIndex).getY();
                double toX = nodes.get(toNodeIndex).getX();
                double toY = nodes.get(toNodeIndex).getY();

                double distance = distanceToLineSegment(mouseX, mouseY, fromX, fromY, toX, toY);

                if (distance < 5) {
                    edge.setSelected(!edge.isSelected());
                    if (edge.isSelected()) {
                        checkSolution(edge);
                    }
                    if (this.checkFinish(edges)) {
                        gc.clearRect(0, 0, 600, 600);
                        Label finishLabel = new Label("Your Score: " + this.timerLabel.getText());
                        finishLabel.setLayoutX(270);
                        finishLabel.setLayoutY(300);
                        Pane root = new Pane(finishLabel);
                        Scene scene = new Scene(root, 600, 600);

                        primaryStage.setTitle("Steinerbaum");
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    } else {

                    }
                    break;
                }
            }

            redrawCanvas(gc);

            // set value
            int totalValue = 0;
            for (Edge edge : edges) {
                if (edge.isSelected()) {
                    totalValue += edge.getValue();
                }
            }
            valueLabel.setText(Integer.toString(totalValue));

    }

    private double distanceToLineSegment(double x, double y, double x1, double y1, double x2, double y2) {
        double segmentLength = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double segmentDirectionX = (x2 - x1) / segmentLength;
        double segmentDirectionY = (y2 - y1) / segmentLength;

        double pointVectorX = x - x1;
        double pointVectorY = y - y1;

        double distanceAlongSegment = pointVectorX * segmentDirectionX + pointVectorY * segmentDirectionY;
        distanceAlongSegment = Math.max(0, Math.min(distanceAlongSegment, segmentLength));

        double closestPointX = x1 + distanceAlongSegment * segmentDirectionX;
        double closestPointY = y1 + distanceAlongSegment * segmentDirectionY;

        double distance = Math
                .sqrt((x - closestPointX) * (x - closestPointX) + (y - closestPointY) * (y - closestPointY));

        return distance;
    }

    private void updateTimer() {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedMillis / 1000 + timeToAdd;
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
