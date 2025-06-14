package controller;

import domain.DirectedAdjacencyListGraph;
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

public class DirectedAdjListGraphViewController {
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TextArea ta_toString;
    private String[] letters;
    DirectedAdjacencyListGraph graph;
    private Alert alert;
    private TextInputDialog dialog;
    DirectedAdjListGraphVisualization visualization;

    public void initialize() throws GraphException, ListException {
        graph = new DirectedAdjacencyListGraph(26); // 26 letras
        // Letras del abecedario
        letters = new String[]{
                "A","B","C","D","E","F","G","H","I","J","K","L","M",
                "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
        };
        setGraph();
        generateEdges();
        displayGraph();
        alert = FXUtility.alert("AdjListGraph", null);
        dialog = FXUtility.dialog("AdjListGraph", null);
    }

    private void setGraph() throws GraphException, ListException {
        int count = 1;
        while (count <= 10){
            int randomIndex = Utility.random(26)-1;
            if (graph.isEmpty()) {
                graph.addVertex(letters[randomIndex]);
                count++;
            } else{
                String currentLetter = letters[randomIndex];
                if (!graph.containsVertex(currentLetter)){
                    graph.addVertex(currentLetter);
                    count++;
                }
            }
        }
    }

    private void displayGraph() throws ListException, GraphException {
        paneGraph.getChildren().clear();
        visualization = new DirectedAdjListGraphVisualization(graph);
        visualization.displayGraph();
        paneGraph.getChildren().add(visualization);
    }

    private void generateEdges() throws GraphException, ListException {
        int countEdges = 0;
        while (countEdges < 15){
            Object a = graph.getVertexByIndex(Utility.random(graph.size())-1).data;
            Object b = graph.getVertexByIndex(Utility.random(graph.size())-1).data;

            if (!a.equals(b) && !graph.containsEdge(a, b)){
                int weight = Utility.random(49) + 1;
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
        graph = new DirectedAdjacencyListGraph(26);
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
            } catch (GraphException | ListException | RuntimeException e) {
                alert.setContentText("Please put this format 'A'-'B'");
                alert.showAndWait();
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
            } catch (GraphException | ListException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
