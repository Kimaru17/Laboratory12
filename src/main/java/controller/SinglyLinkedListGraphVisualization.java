package controller;

import domain.*;
import domain.list.ListException;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SinglyLinkedListGraphVisualization extends Pane {

    private int[][] adjacencyMatrix;
    private SinglyLinkedListGraph graph;
    private static final double WIDTH = 600;
    private static final double HEIGHT = 300;

    private Circle[] circles;
    private Line selectedLine = null; // solo una línea seleccionada
    private Text selectedInfoText = null; // solo una info visible

    public SinglyLinkedListGraphVisualization(SinglyLinkedListGraph graph) throws ListException {
        this.graph = graph;
        this.setWidth(1050);
        this.setHeight(1500);
        setStatus("Tree is empty");
        adjacencyMatrix = new int[graph.size()][graph.size()];
        circles = new Circle[11]; // Puedes ajustar el tamaño si hay más nodos
    }

    public void setStatus(String msg){
        getChildren().add(new Text(20, 20, msg));
    }

    public void displayGraph() throws ListException, GraphException {
        this.getChildren().clear();
        selectedLine = null;
        selectedInfoText = null;
        displayVertex();
        displayEdges();
    }

    private void displayVertex() throws ListException {
        double angleStep = 360.0 / graph.size();
        double offsetX = WIDTH * 0.5;
        double offsetY = HEIGHT * 1.0;
        double radius = 200;

        for (int i = 1; i <= graph.size(); i++) {
            double angle = Math.toRadians(angleStep * i);
            double x = offsetX + radius * Math.cos(angle);
            double y = offsetY + radius * Math.sin(angle);
            Circle circle = new Circle(x, y, 20, Color.DARKMAGENTA);
            circles[i] = circle;
            circle.setId("vertex-" + i);
            this.getChildren().add(circle);

            Text text = new Text(graph.getVertexByIndex(i).data + "");
            text.setX(x - 7);
            text.setY(y + 4);
            text.setFill(Color.WHITE);
            text.setFont(new Font(14));
            this.getChildren().add(text);
        }
    }

    private void displayEdges() throws GraphException, ListException {
        for (int i = 1; i <= graph.size(); i++) {
            for (int j = i + 1; j <= graph.size(); j++) {

                if (i == j) continue;

                Object a = graph.getVertexByIndex(i).data;
                Object b = graph.getVertexByIndex(j).data;

                if (graph.containsEdge(a, b)){
                    Circle circleA = (Circle) this.lookup("#vertex-" + i);
                    Circle circleB = (Circle) this.lookup("#vertex-" + j);
                    Line line = new Line(
                            circleA.getCenterX(), circleA.getCenterY(),
                            circleB.getCenterX(), circleB.getCenterY());

                    line.setStrokeWidth(3.5);
                    line.setCursor(Cursor.HAND);

                    // Selección exclusiva
                    line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            // Quita selección previa
                            if (selectedLine != null && selectedLine != line) {
                                selectedLine.setStroke(Color.BLACK);
                            }
                            // Quita texto previo
                            if (selectedInfoText != null) {
                                getChildren().remove(selectedInfoText);
                            }
                            // Marca nueva selección
                            selectedLine = line;
                            line.setStroke(Color.RED);
                            selectedInfoText = createEdgeInfoText(a, b);
                            getChildren().add(selectedInfoText);
                        }
                    });
                    line.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (selectedLine != line) {
                                line.setStroke(Color.GREEN);
                            }
                        }
                    });
                    line.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (selectedLine != line) {
                                line.setStroke(Color.BLACK);
                            }
                        }
                    });

                    this.getChildren().add(line);
                    line.toBack();
                }
            }
        }
    }

    private Text createEdgeInfoText(Object a, Object b) {
        try {
            Object weight = graph.getWeightEdges(a, b);
            Text text = new Text("Edge between the vertexes: " + a + " ... " + b + ". Weight: " + weight);
            text.setX(30);
            text.setY(30);
            text.setFont(new Font(16));
            text.setFill(Color.BLUE);
            text.setWrappingWidth(500);
            return text;
        } catch (Exception e) {
            return new Text("Error showing edge info.");
        }
    }
}
