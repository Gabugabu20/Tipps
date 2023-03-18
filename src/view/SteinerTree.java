package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Edge;
import model.Game;
import model.Message;
import model.Node;

/**
 * 
 * The main class for the SteinerTree game.
 * 
 * Extends the javafx.application.Application class and overrides the start()
 * method.
 */
public class SteinerTree extends Application {

    private GraphicsContext gc;
    private Stage primaryStage;
    private Canvas canvas;
    private Game game;

    // GUI Elements
    // Labels
    private Label actualValueLabel;
    private Label valueLabel;
    private Label nodesLabel;
    private Label timerTitleLabel;
    private Label timerLabel;
    private Label finishLabel;
    private Label messageTitleLabel;
    private Label messageLabel;
    // Buttons
    private Button tippButton;
    private Button nextButton;

    // nodes and edges from game logic
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Edge> solution;
    private ArrayList<Integer> nodesToConnect;

    // Timer
    private Timeline timeline;
    private long startTime;
    private long timeToAdd = 0;
    private long timeAdmonition = 15;
    private boolean tippOn = false;

    // Canvas size
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;

    private int currentLevel = 1;
    private boolean finished = false;

    public SteinerTree(Game game) {
        this.game = game;
    }

    /**
     * 
     * Overrides the start method of Application class.
     * 
     * Initializes the canvas and stage and sets the scene.
     * 
     * Also initializes all GUI elements like labels and buttons.
     * 
     * Handles the events of tippButton and canvas.
     * 
     * Initializes the timeline.
     * 
     * Calls the drawCanvas, updateValue methods.
     * 
     * @param primaryStage the main stage of the game
     */

    @Override
    public void start(Stage primaryStage) {
        // initialize canvas and stage
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.primaryStage = primaryStage;

        // initializes all GUI elements like labels and buttons
        initializeGUIElements();

        // press the tipp button
        tippButton.setOnAction(event -> handleTippButtonPressed(event));

        // press the next button
        nextButton.setOnAction(event -> handleNextButtonPressed(event));

        // click on edge
        canvas.setOnMouseClicked(event -> handleEdgeClick(event));

        // starts level
        startLevel(currentLevel);
    }

    /**
     * 
     * Initializes all the GUI elements like labels and buttons.
     */
    private void initializeGUIElements() {
        actualValueLabel = new Label("Aktueller Wert:");
        actualValueLabel.setLayoutX(20);
        actualValueLabel.setLayoutY(20);

        valueLabel = new Label();
        valueLabel.setLayoutX(160);
        valueLabel.setLayoutY(20);

        nodesLabel = new Label();
        nodesLabel.setLayoutX(250);
        nodesLabel.setLayoutY(60);

        timerTitleLabel = new Label("Zeit: ");
        timerTitleLabel.setLayoutX(250);
        timerTitleLabel.setLayoutY(20);

        timerLabel = new Label();
        timerLabel.setLayoutX(300);
        timerLabel.setLayoutY(20);

        messageTitleLabel = new Label("Hinweis: ");
        messageTitleLabel.setLayoutX(250);
        messageTitleLabel.setLayoutY(100);

        messageLabel = new Label();
        messageLabel.setLayoutX(340);
        messageLabel.setLayoutY(100);

        tippButton = new Button("Tipp +" + getTimeAdmonition());
        tippButton.setLayoutX(20);
        tippButton.setLayoutY(80);
        tippButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tippOn));

        nextButton = new Button("Nächster Level");
        nextButton.setLayoutX(WIDTH / 2);
        nextButton.setLayoutY(HEIGHT / 3);
        nextButton.disableProperty().bind(Bindings.createBooleanBinding(() -> finished));
    }

    /**
     * 
     * Initializes the timer in the background
     */
    private void initializeTimer() {
        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        startTime = System.currentTimeMillis();
        timeline.play();

        timeToAdd = 0;
        timeAdmonition = 15;
        // set tipp button text
        tippButton.setText("Tipp +" + getTimeAdmonition());
    }

    private void startLevel(int currentLevel) {
        // set nodes, edges, solution and nodes to be connected for the game
        this.nodes = game.getNodes();
        this.edges = game.getEdges();
        this.solution = game.getSolution(currentLevel);
        this.nodesToConnect = game.getNodesToConnect(currentLevel);

        // initialize timeline
        initializeTimer();

        for (Edge edge : edges) {
            edge.setSelected(false);
        }

        nodesLabel.setText("Diese Knoten zusammen verbinden: "
                + nodesToConnect.stream().sorted().map(Object::toString).collect(Collectors.joining(", ")) + ".");

        // draw canvas
        drawCanvas();
        // update value
        updateValue();

        // set font size and boldness for the root pane
        Font font = Font.font("Arial", FontWeight.BOLD, 20);
        Pane root = new Pane(canvas, tippButton, actualValueLabel, valueLabel, nodesLabel,
                timerLabel, timerTitleLabel, messageTitleLabel, messageLabel);
        root.setStyle("-fx-font: " + font.getSize() + "px \"" + font.getFamily() + "\";");

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Steinerbaum");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 
     * Draws the edges and nodes on the canvas.
     */
    private void drawCanvas() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        drawEdges();
        drawNodes();

        tippButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tippOn));
    }

    /**
     * 
     * Draws the edges on the canvas.
     * Sets the stroke color to BLACK and the line width to 3. Iterates through all
     * the edges and sets the stroke color to
     * the color of the edge. Then, it gets the indices of the two nodes of the edge
     * and the corresponding coordinates.
     * Finally, it draws the line between the two nodes on the canvas.
     */
    private void drawEdges() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        for (Edge edge : edges) {
            gc.setStroke(edge.getColor());
            int fromNodeIndex = edge.getNode1().getNumber() - 1;
            int toNodeIndex = edge.getNode2().getNumber() - 1;
            double fromX = nodes.get(fromNodeIndex).getX();
            double fromY = nodes.get(fromNodeIndex).getY();
            double toX = nodes.get(toNodeIndex).getX();
            double toY = nodes.get(toNodeIndex).getY();
            gc.strokeLine(fromX, fromY, toX, toY);
        }
    }

    /**
     * 
     * Draws the nodes on the canvas.
     * Sets the line width to 1. Iterates through all the nodes and sets the fill
     * color to the color of the node. Then, it
     * fills an oval with the center at the coordinates of the node on the canvas.
     * It then strokes the oval and sets the text
     * color to WHITE. It then adds the text of the node number at the center of the
     * oval.
     * 
     * @param node the node to get the color for
     * @return the color of the node
     */
    private void drawNodes() {
        gc.setLineWidth(1);

        for (Node node : nodes) {
            double x = node.getX();
            double y = node.getY();
            gc.setFill(getNodeColor(node));
            gc.fillOval(x - 15, y - 15, 30, 30);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 15, y - 15, 30, 30);
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(Integer.toString(node.getNumber()), x - 5, y + 8);
            gc.setFill(Color.BLACK);
        }
    }

    /**
     * 
     * Gets the color of the node.
     * If the node number is in the list of nodes to connect, the color of the node
     * is BLUE. Otherwise, the color is BLACK.
     * 
     * @param node the node to get the color for
     * @return the color of the node
     */
    private Color getNodeColor(Node node) {
        return nodesToConnect.contains(node.getNumber()) ? Color.BLUE : Color.BLACK;
    }

    /**
     * 
     * Handles the event when the user clicks on an edge.
     * It checks if the edge is part of the solution and updates the score.
     * 
     * @param event The event when the user clicks on an edge.
     */
    private void handleEdgeClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

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
                // removes the tipp
                removeTipp();

                edge.setSelected(!edge.isSelected());
                if (edge.isSelected()) {
                    checkSolution(edge);
                }
                if (checkFinish(edges)) {
                    showFinish();
                }
                break;
            }
        }

        if (hasCycle()) {
            messageLabel.setText(Message.KREIS.getMessage());
        } else if (!hasCycle() && messageLabel.getText().equals(Message.KREIS.getMessage())) {
            messageLabel.setText("");
        }

        // redraws canvas
        drawCanvas();
        // update value
        updateValue();
    }

    /**
     * 
     * Handles the event when the user clicks the tipp button.
     * It finds the edge with the tipp value as true and updates the score.
     * 
     * @param event The event when the user presses the tipp button.
     */
    private void handleTippButtonPressed(ActionEvent event) {
        final int TIMEADMONITIONSUMMAND = 15;
        final int FIRSTEDGE = 0;

        // admonition time
        timeToAdd += timeAdmonition;
        timeAdmonition += TIMEADMONITIONSUMMAND;

        // saves all the edges which need to be chosen
        ArrayList<Edge> edgesToBeChosen = new ArrayList<>();
        for (Edge edge : edges) {
            if (!edge.isSelected() && solution.contains(edge)) {
                edgesToBeChosen.add(edge);
            }
        }

        // saves all the edges which needs to be removed
        ArrayList<Edge> edgesToBeRemoved = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.isSelected() && !solution.contains(edge)) {
                edgesToBeRemoved.add(edge);
            }
        }

        // sets an edge that is shown to the player
        if (!edges.stream().anyMatch(obj -> obj.isTipp()) && edgesToBeChosen.size() != 0) {
            Collections.shuffle(edgesToBeChosen);
            edgesToBeChosen.get(FIRSTEDGE).setTipp(true);
            tippOn = true;
            messageLabel.setText(Message.ADDEDGE.getMessage());
        } else if (edgesToBeChosen.size() == 0 && edgesToBeRemoved.size() != 0) {
            Collections.shuffle(edgesToBeRemoved);
            edgesToBeRemoved.get(FIRSTEDGE).setTipp(true);
            tippOn = true;
            messageLabel.setText(Message.REMOVEEDGE.getMessage());
        }

        // set tipp button text
        tippButton.setText("Tipp +" + getTimeAdmonition());

        // redraw canvas
        drawCanvas();
        // update value
        updateValue();
    }

    private void handleNextButtonPressed(ActionEvent event) {
        currentLevel++;
        startLevel(currentLevel);
    }

    /**
     * 
     * Checks whether the given edge is part of the solution or not.
     * 
     * @param edge The edge to be checked.
     */
    private void checkSolution(Edge edge) {
        if (solution.contains(edge)) {
            edge.setSolution(true);
        }
    }

    /**
     * 
     * Sets the tipp value of an edge to false, it was previously true.
     */
    private void removeTipp() {
        edges.stream()
                .filter(obj -> obj.isTipp())
                .findFirst()
                .ifPresent(obj -> obj.setTipp(false));

        tippOn = false;
        messageLabel.setText("");
    }

    /**
     * 
     * Checks whether the player has finished the game or not.
     * 
     * @param edges The list of edges to be checked.
     * @return True if the player has chosen all the correct edges, false otherwise.
     */
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
        if (nodesFound == solution.size()) {
            return true;
        }
        return false;
    }

    /**
     * 
     * Shows a new window with the score of the player (time).
     */
    private void showFinish() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        finishLabel = new Label("Your Score: " + this.timerLabel.getText());
        finishLabel.setLayoutX(WIDTH / 2);
        finishLabel.setLayoutY(HEIGHT / 2);

        Pane root = new Pane();

        if (new Game().getNumberOfLevels() == currentLevel) {
            root = new Pane(finishLabel);
        } else {
            root = new Pane(finishLabel, nextButton);
        }
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Steinerbaum");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 
     * Updates the value of the steinertree drawn by the player.
     */
    private void updateValue() {
        // set value
        int totalValue = 0;
        for (Edge edge : edges) {
            if (edge.isSelected()) {
                totalValue += edge.getValue();
            }
        }
        valueLabel.setText(Integer.toString(totalValue));
    }

    /**
     * 
     * Updates the timer in the background.
     */
    private void updateTimer() {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedMillis / 1000 + timeToAdd;
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * 
     * Calculates the distance between a point (x,y) and a line segment defined by
     * two points (x1,y1) and (x2,y2).
     * The method returns the minimum distance between the point and the line
     * segment.
     * 
     * @param x  The x-coordinate of the point
     * @param y  The y-coordinate of the point
     * @param x1 The x-coordinate of the first endpoint of the line segment
     * @param y1 The y-coordinate of the first endpoint of the line segment
     * @param x2 The x-coordinate of the second endpoint of the line segment
     * @param y2 The y-coordinate of the second endpoint of the line segment
     * @return The minimum distance between the point and the line segment
     */
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

    /**
     * 
     * Returns the time admonition formatted as Xm Ys.
     * For example: 1m 30s
     */
    private String getTimeAdmonition() {
        long minutes = timeAdmonition / 60;
        long seconds = timeAdmonition % 60;
        return String.format("%dm %ds", minutes, seconds);
    }

    /**
     * 
     * Checks if the selected edges in the graph form a cycle.
     * 
     * @return true if a cycle is formed, false otherwise
     */
    private boolean hasCycle() {
        // Create an adjacency list to store the nodes and their neighbors
        Map<Node, List<Node>> adjList = new HashMap<>();

        // Create a list of selected edges
        List<Edge> selectedEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.isSelected()) {
                selectedEdges.add(edge);
            }
        }

        // If there are less than 2 selected edges, no cycle can be formed
        if (selectedEdges.size() < 2) {
            return false;
        }

        // Create the adjacency list by adding the nodes and their neighbors from the
        // selected edges
        for (Edge edge : selectedEdges) {
            Node node1 = edge.getNode1();
            Node node2 = edge.getNode2();
            if (!adjList.containsKey(node1)) {
                adjList.put(node1, new ArrayList<>());
            }
            if (!adjList.containsKey(node2)) {
                adjList.put(node2, new ArrayList<>());
            }
            adjList.get(node1).add(node2);
            adjList.get(node2).add(node1);
        }

        // Create a set to keep track of visited nodes and a map to keep track of their
        // parent node in the DFS tree
        Set<Node> visited = new HashSet<>();
        Map<Node, Node> parent = new HashMap<>();

        // Create a stack to perform depth-first search starting from the first node in
        // the first selected edge
        Stack<Node> stack = new Stack<>();
        Node startNode = selectedEdges.get(0).getNode1();
        stack.push(startNode);
        parent.put(startNode, null);
        while (!stack.empty()) {
            Node currNode = stack.pop();
            visited.add(currNode);
            List<Node> neighbors = adjList.get(currNode);
            for (Node neighbor : neighbors) {
                // If the neighbor node has not been visited, add it to the stack and set its
                // parent to the current node
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                    parent.put(neighbor, currNode);
                }
                // If the neighbor node has been visited and it is not the parent of the current
                // node, a cycle is formed
                else if (parent.get(currNode) != neighbor) {
                    return true;
                }
            }
        }

        // No cycle is formed
        return false;
    }

}
