package controller;

import domain.DirectedAdjacencyListGraph;
import domain.GraphException;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import util.Utility;
import util.FXUtility;

public class DirectedAdjListGraphOperationsViewController
{
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TextArea ta_toString;

    private String[] letters;
    DirectedAdjacencyListGraph graph;
    private Alert alert;
    DirectedAdjListGraphVisualization visualization;

    @javafx.fxml.FXML
    public void initialize() throws GraphException, ListException {
        graph = new DirectedAdjacencyListGraph(26); // Para el abecedario
        letters = new String[]{
                "A","B","C","D","E","F","G","H","I","J","K","L","M",
                "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
        };
        setGraph();
        generateEdges();
        displayGraph();
        alert = FXUtility.alert("AdjListGraph", null);
        ta_toString.setVisible(true);
        ta_toString.setText(graph.toString());
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
            int aIndex = Utility.random(graph.size())-1;
            int bIndex = Utility.random(graph.size())-1;
            Object a = graph.getVertexByIndex(aIndex).data;
            Object b = graph.getVertexByIndex(bIndex).data;
            if (!a.equals(b) && !graph.containsEdge(a, b)){
                int weight = Utility.random(49) + 1;
                graph.addEdgeWeight(a, b, weight);
                countEdges++;
            }
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        graph = new DirectedAdjacencyListGraph(26);
        setGraph();
        generateEdges();
        displayGraph();
        ta_toString.setVisible(true);
        ta_toString.setText(graph.toString());
    }

    @javafx.fxml.FXML
    public void clearOnAction(ActionEvent actionEvent) {
        graph.clear();
        ta_toString.setVisible(false);
        ta_toString.setText("");
        paneGraph.getChildren().clear();
    }

    @javafx.fxml.FXML
    public void addVertexOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()){
            int randomIndex = Utility.random(26) - 1;
            String currentLetter = letters[randomIndex];
            graph.addVertex(currentLetter);
            displayGraph();
            ta_toString.setVisible(true);
            ta_toString.setText(graph.toString());
        } else if (graph.size() == 10) {
            alert.setContentText("The graph is full");
            alert.showAndWait();
        } else {
            boolean added = false;
            while (!added) {
                int randomIndex = Utility.random(26) - 1;
                String currentLetter = letters[randomIndex];
                if (!graph.containsVertex(currentLetter)) {
                    graph.addVertex(currentLetter);
                    added = true;
                }
            }
            displayGraph();
            ta_toString.setVisible(true);
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void removeVertexOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()) {
            alert.setContentText("Can't remove more vertex \nbecause graph is empty");
            alert.showAndWait();
        } else if (graph.size() == 1) {
            graph.clear();
            ta_toString.setVisible(false);
            ta_toString.setText("");
            paneGraph.getChildren().clear();
            alert.setContentText("Can't remove more vertex \nbecause graph is empty");
            alert.showAndWait();
        } else {
            boolean removed = false;
            while (!removed) {
                int randomIndex = Utility.random(26) - 1;
                String currentLetter = letters[randomIndex];
                if (graph.containsVertex(currentLetter)) {
                    graph.removeVertex(currentLetter);
                    removed = true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void removeEdgesWeightOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (graph.isEmpty()) {
            alert.setContentText("Can't remove more edges \nbecause graph is empty");
            alert.showAndWait();
        } else if (graph.size() == 1) {
            alert.setContentText("Can't remove more edges \nbecause there is only one vertex");
            alert.showAndWait();
        } else {
            boolean removed = false;
            while (!removed) {
                int aIndex = Utility.random(graph.size())-1;
                int bIndex = Utility.random(graph.size())-1;
                Object a = graph.getVertexByIndex(aIndex).data;
                Object b = graph.getVertexByIndex(bIndex).data;
                if (graph.containsEdge(a,b)) {
                    graph.removeEdge(a, b);
                    removed = true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void addEdgesWeightOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (graph.isEmpty()){
            alert.setContentText("Can't add edges and weight because the graph is empty");
            alert.showAndWait();
        } else if (graph.size() == 1) {
            alert.setContentText("Can't add more edges \nbecause there is only one vertex");
            alert.showAndWait();
        } else {
            boolean added = false;
            while (!added) {
                int aIndex = Utility.random(graph.size())-1;
                int bIndex = Utility.random(graph.size())-1;
                Object a = graph.getVertexByIndex(aIndex).data;
                Object b = graph.getVertexByIndex(bIndex).data;
                if (!graph.containsEdge(a,b) && !a.equals(b)) {
                    int weight = Utility.random(200) + 200;
                    graph.addEdgeWeight(a, b, weight);
                    added = true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }
}
