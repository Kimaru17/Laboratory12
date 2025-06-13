package controller;

import domain.AdjacencyListGraph;
import domain.GraphException;
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

public class AdjListGraphVisualization extends Pane {

    private AdjacencyListGraph graph;
    private static final double WIDTH = 600;
    private static final double HEIGHT = 300;

    private Circle[] circles;
    private Line selectedLine = null; // solo una línea puede estar seleccionada
    private Text selectedInfoText = null; // solo una info de arista visible

    public AdjListGraphVisualization(AdjacencyListGraph graph) throws ListException {
        this.graph = graph;
        this.setWidth(1050);
        this.setHeight(1500);
        circles = new Circle[Math.max(graph.size() + 2, 12)];
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
            Circle circle = new Circle(x, y, 20, Color.STEELBLUE);
            circles[i] = circle;
            circle.setId("vertex-" + i);
            this.getChildren().add(circle);

            Text text = new Text(graph.getVertexByIndex(i - 1).data + "" );
            text.setX(x - 7);
            text.setY(y + 4);
            text.setFill(Color.BLACK);
            text.setFont(new Font(14));
            text.setWrappingWidth(75);
            this.getChildren().add(text);
        }
    }

    private void displayEdges() throws GraphException, ListException {
        for (int i = 1; i <= graph.size(); i++) {
            for (int j = i + 1; j <= graph.size(); j++) {

                if (i == j) continue;

                Object a = graph.getVertexByIndex(i - 1).data;
                Object b = graph.getVertexByIndex(j - 1).data;

                if (graph.containsEdge(a, b)) {
                    Circle circleA = (Circle) this.lookup("#vertex-" + i);
                    Circle circleB = (Circle) this.lookup("#vertex-" + j);
                    Line line = new Line(circleA.getCenterX(),
                            circleA.getCenterY(),
                            circleB.getCenterX(),
                            circleB.getCenterY());

                    line.setStrokeWidth(3.5);
                    line.setCursor(Cursor.HAND);

                    // Manejo de eventos exclusivo para selección única:
                    line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            // Si hay otra línea seleccionada, la des-selecciona
                            if (selectedLine != null && selectedLine != line) {
                                selectedLine.setStroke(Color.BLACK);
                            }
                            // Si había info, la borra
                            if (selectedInfoText != null) {
                                getChildren().remove(selectedInfoText);
                            }
                            // Selecciona la nueva línea y muestra info
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

    // Crea el texto informativo de la arista seleccionada
    private Text createEdgeInfoText(Object a, Object b) {
        try {
            Object weight = graph.getWeightEdges(a, b);
            Text text = new Text("Edge between: " + a + " - " + b + ". Weight: " + weight);
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
