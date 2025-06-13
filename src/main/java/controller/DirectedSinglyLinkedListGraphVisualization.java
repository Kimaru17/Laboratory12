package controller;

import domain.DirectedSinglyLinkedListGraph;
import domain.GraphException;
import domain.SinglyLinkedListGraph;
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

public class DirectedSinglyLinkedListGraphVisualization extends Pane {

    private int[][] adjacencyMatrix;
    private DirectedSinglyLinkedListGraph graph;
    private static final double WIDTH = 600;
    private static final double HEIGHT = 300;

    Circle circles[];
    int visitedEdges = 0;


    public DirectedSinglyLinkedListGraphVisualization(DirectedSinglyLinkedListGraph graph) throws ListException {
        this.graph = graph;
        this.setWidth(1050);
        this.setHeight(1500);
        setStatus("Tree is empty");
        adjacencyMatrix = new int[graph.size()][graph.size()];
    }


    public void setStatus(String msg){
        getChildren().add(new Text(20, 20, msg));
    }

    public void displayGraph() throws ListException, GraphException {
        this.getChildren().clear();
        displayVertex();
        displayEdges();
    }
    private void displayVertex() throws ListException {
        // Configurar los parámetros del círculo
        double angleStep = 360.0 / graph.size();
        double offsetX = WIDTH * 0.5; // Ajuste para centrar más a la derecha
        double offsetY = HEIGHT * 1.0;
        double radius = 200; // hace el círculo más grande

        circles = new Circle[11];

        // Agregar vértices al grafo y colocarlos en el círculo
        for (int i = 1; i <= graph.size(); i++) {
            //graph.addVertex(i);
            double angle = Math.toRadians(angleStep * i);
            double x = offsetX + radius * Math.cos(angle);
            double y = offsetY + radius * Math.sin(angle);
            Circle circle = new Circle(x, y, 20, Color.STEELBLUE); // Hacer el círculo más grande
            circles[i] = circle;
            circle.setId("vertex-" + i); // Set an ID to find this circle later
            this.getChildren().add(circle);

            Text text = new Text(graph.getVertexByIndex(i).data + "" );
            text.setX(x - 7); // Ajustar la posición del número dentro del círculo
            text.setY(y + 4); // Ajustar la posición del número dentro del círculo
            text.setFill(Color.BLACK); // Cambiar el color del número a blanco
            text.setFont(new Font(14));
            text.setWrappingWidth(75);
            this.getChildren().add(text);

        }
    }
    private void displayEdges() throws GraphException, ListException {
        for (int i = 1; i <= graph.size(); i++) {
            for (int j = i + 1; j <= graph.size(); j++) {

                if (i==j) continue;

                Object a = graph.getVertexByIndex(i).data;
                Object b = graph.getVertexByIndex(j).data;

                if (graph.containsEdge(a, b)){
                    Circle circleA = (Circle) this.lookup("#vertex-" + i);
                    Circle circleB = (Circle) this.lookup("#vertex-" + j);
                    Line line = new Line(circleA.getCenterX(),
                            circleA.getCenterY(),
                            circleB.getCenterX(),
                            circleB.getCenterY());

                    line.setStrokeWidth(3.5);
                    line.setCursor(Cursor.HAND);

                    line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            line.setStroke(Color.RED);
                            showEdge(a, b);
                            line.setUserData("clicked"); // Marcar la línea como "clickeada"
                        }
                    });
                    line.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (!"clicked".equals(line.getUserData())) { // Solo cambiar color si no está clickeada
                                line.setStroke(Color.GREEN);
                            }
                        }
                    });
                    line.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (!"clicked".equals(line.getUserData())) { // Solo cambiar color si no está clickeada
                                line.setStroke(Color.BLACK);
                            }
                        }
                    });

                    this.getChildren().add(line);

                    // Asegurarse de que las líneas estén detrás de los círculos
                    line.toBack();
                }


            }
        }
    }

    private void showEdge(Object a, Object b){
        try {
            if (graph.containsEdge(a, b)){
                Object weight = graph.getWeightEdges(a, b);
                Text text = new Text("Edge between the vertexes: " + a + "... " + b + ". Weight: " + weight);
                text.setX(30);
                text.setY(30);
                text.setFont(new Font(16));
                text.setFill(Color.BLUE);
                text.setWrappingWidth(500);

                if (visitedEdges > 0)
                    //this.getChildren().removeLast();
                this.getChildren().add(text);
                visitedEdges++;
            }
        } catch (GraphException e) {
            throw new RuntimeException(e);
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }
}
