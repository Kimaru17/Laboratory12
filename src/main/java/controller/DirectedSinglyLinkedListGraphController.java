package controller;

import domain.DirectedSinglyLinkedListGraph;
import domain.GraphException;
import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import util.Utility;
import util.FXUtility;

import java.util.Optional;

public class DirectedSinglyLinkedListGraphController {
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TextArea ta_toString;
    private String[] monuments;
    DirectedSinglyLinkedListGraph graph;
    private Alert alert;
    private TextInputDialog dialog;
    DirectedSinglyLinkedListGraphVisualization visualization;

    public void initialize() throws GraphException, ListException {
        graph = new DirectedSinglyLinkedListGraph();
        monuments = new String[]{ "Great Wall of China", "Taj Mahal", "Machu Picchu", "Colosseum",
                "Pyramids of Giza", "Eiffel Tower", "Stonehenge", "Statue of Liberty", "Alhambra",
                "Temple of Kukulcan", "Angkor Wat", "Parthenon", "Petra", "Christ the Redeemer",
                "Big Ben", "Neuschwanstein Castle", "Mount Rushmore", "Sagrada Familia",
                "Leaning Tower of Pisa", "Acropolis of Athens", "Tower Bridge",
                "Palace of Versailles", "Notre Dame Cathedral", "St. Peter's Basilica",
                "St. Basil's Cathedral", "Brandenburg Gate", "Chichen Itza", "Himeji Castle",
                "Petronas Towers", "Sydney Opera House"};
        setGraph();
        generateEdges();
        displayGraph();
        alert = FXUtility.alert("SinglyLinkedListGraph", null);
        dialog = FXUtility.dialog("SinglyLinkedListGraph", null);
    }

    private void setGraph() throws GraphException, ListException {
        int count = 1;
        while (count <= 10){
            int randomIndex = util.Utility.random(30)-1;
            if (graph.isEmpty()) {
                graph.addVertex(monuments[randomIndex]);
                count++;
            } else{
                String currentName = monuments[randomIndex];
                if (!graph.containsVertex(currentName)){
                    graph.addVertex(currentName);
                    count++;
                }
            }
        }
    }
    private void displayGraph() throws ListException, GraphException {
        paneGraph.getChildren().clear();
        visualization = new DirectedSinglyLinkedListGraphVisualization(graph);
        visualization.displayGraph();
        paneGraph.getChildren().add(visualization);
    }

    private void generateEdges() throws GraphException, ListException {
        int countEdges = 0;
        while (countEdges < 15){
            Object a = graph.getVertexByIndex(Utility.random(10)).data;
            Object b = graph.getVertexByIndex(Utility.random(10)).data;

            if (!a.equals(b) && !graph.containsEdge(a, b)){
                int weight = util.Utility.random(200) + 200;
                graph.addEdgeWeight(a, b, weight);
                countEdges++;
            }
        }

    }

    @javafx.fxml.FXML
    public void toStringOnAction(ActionEvent actionEvent) {
        ta_toString.setText(graph.toString());
        ta_toString.setVisible(true);
    }

    @javafx.fxml.FXML
    public void dfsTourOnAction(ActionEvent actionEvent) throws GraphException, ListException, StackException {
        alert.setContentText("DFS Tour: " + graph.dfs().substring(0, graph.dfs().length()-2));
        alert.showAndWait();
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        graph = new DirectedSinglyLinkedListGraph();
        setGraph();
        generateEdges();
        displayGraph();
        ta_toString.setVisible(false);
    }

    @javafx.fxml.FXML
    public void bfsTourOnAction(ActionEvent actionEvent) throws GraphException, QueueException, ListException {
        alert.setContentText("DFS Tour: " + graph.bfs().substring(0, graph.bfs().length()-2));
        alert.showAndWait();
    }

    @javafx.fxml.FXML
    public void containsEdgeOnAction(ActionEvent actionEvent) {
        dialog.setContentText("Type the edge to look in format: A-B");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(vertex ->{
            try {
                String[] elements = vertex.split("-");

                if (graph.containsEdge((elements[0]),(elements[1]))){
                    alert.setContentText("The elements [" + elements[0] + ", " + elements[1] + "] have an edge");
                    alert.showAndWait();
                } else {
                    alert.setContentText("The elements [" + elements[0] + ", " + elements[1] + "] don't have an edge");
                    alert.showAndWait();
                }
            } catch (GraphException e) {
                throw new RuntimeException(e);
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @javafx.fxml.FXML
    public void containsVertexOnAction(ActionEvent actionEvent) {
        dialog.setContentText("Type the vertex to look");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(vertex ->{
            try {
                if (graph.containsVertex(vertex)){
                    alert.setContentText("The element [" + vertex + "] exists in the graph");
                    alert.showAndWait();
                } else {
                    alert.setContentText("The element [" + vertex + "] doesn't exist in the graph");
                    alert.showAndWait();
                }
            } catch (GraphException e) {
                throw new RuntimeException(e);
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
