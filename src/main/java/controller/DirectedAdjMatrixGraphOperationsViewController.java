package controller;

import domain.DirectedAdjacencyMatrixGraph;
import domain.GraphException;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import util.Utility;
import util.FXUtility;

import java.util.ArrayList;
import java.util.Collections;

public class DirectedAdjMatrixGraphOperationsViewController {
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TextArea ta_toString;

    private ArrayList<Integer> numbers; // 10 números únicos
    DirectedAdjacencyMatrixGraph graph;
    private Alert alert;
    DirectedAdjMatrixGraphVisualization visualization;

    @javafx.fxml.FXML
    public void initialize() throws GraphException, ListException {
        graph = new DirectedAdjacencyMatrixGraph(10);
        setGraph();
        generateEdges();
        displayGraph();
        alert = FXUtility.alert("AdjMatrixGraph", null);
        ta_toString.setVisible(true);
        ta_toString.setText(graph.toString());
    }

    private void setGraph() throws GraphException, ListException {
        numbers = new ArrayList<>();
        ArrayList<Integer> pool = new ArrayList<>();
        for (int i = 0; i < 100; i++) pool.add(i);
        Collections.shuffle(pool);
        for (int i = 0; i < 10; i++) {
            int num = pool.get(i);
            numbers.add(num);
            graph.addVertex(num);
        }
    }

    private void displayGraph() throws ListException, GraphException {
        paneGraph.getChildren().clear();
        visualization = new DirectedAdjMatrixGraphVisualization(graph);
        visualization.displayGraph();
        paneGraph.getChildren().add(visualization);
    }

    private void generateEdges() throws GraphException, ListException {
        int countEdges = 0;
        int n = numbers.size();
        while (countEdges < 15) {
            int idxA = Utility.random(n) - 1;
            int idxB = Utility.random(n) - 1;
            Object a = numbers.get(idxA);
            Object b = numbers.get(idxB);

            if (!a.equals(b) && !graph.containsEdge(a, b)) {
                int weight = Utility.random(49) + 1; // [1, 50]
                graph.addEdgeWeight(a, b, weight);
                countEdges++;
            }
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        graph = new DirectedAdjacencyMatrixGraph(10);
        setGraph();
        generateEdges();
        displayGraph();
        ta_toString.setVisible(true);
        ta_toString.setText(graph.toString());
    }

    @javafx.fxml.FXML
    public void clearOnAction(ActionEvent actionEvent) {
        graph.clear();
        numbers.clear();
        ta_toString.setVisible(false);
        ta_toString.setText("");
        paneGraph.getChildren().clear();
    }

    @javafx.fxml.FXML
    public void addVertexOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()) {
            int newNum = getUnusedRandomNumber();
            numbers.add(newNum);
            graph.addVertex(newNum);
            displayGraph();
            ta_toString.setVisible(true);
            ta_toString.setText(graph.toString());
        } else if (graph.size() == 10) {
            alert.setContentText("The graph is full");
            alert.showAndWait();
        } else {
            boolean added = false;
            while (!added) {
                int newNum = getUnusedRandomNumber();
                if (!graph.containsVertex(newNum)) {
                    numbers.add(newNum);
                    graph.addVertex(newNum);
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
            numbers.clear();
            ta_toString.setVisible(false);
            ta_toString.setText("");
            paneGraph.getChildren().clear();
            alert.setContentText("Can't remove more vertex \nbecause graph is empty");
            alert.showAndWait();
        } else {
            boolean removed = false;
            while (!removed) {
                int idx = Utility.random(numbers.size()) - 1;
                int num = numbers.get(idx);
                if (graph.containsVertex(num)) {
                    graph.removeVertex(num);
                    numbers.remove(Integer.valueOf(num));
                    removed = true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void removeEdgesWeightOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()) {
            alert.setContentText("Can't remove more edges \nbecause graph is empty");
            alert.showAndWait();
        } else if (graph.size() == 1) {
            alert.setContentText("Can't remove more edges \nbecause there is only one vertex");
            alert.showAndWait();
        } else {
            boolean removed = false;
            while (!removed) {
                int idxA = Utility.random(numbers.size()) - 1;
                int idxB = Utility.random(numbers.size()) - 1;
                Object a = numbers.get(idxA);
                Object b = numbers.get(idxB);
                if (graph.containsEdge(a, b)) {
                    graph.removeEdge(a, b);
                    removed = true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void addEdgesWeightOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()) {
            alert.setContentText("Can't add edges and weight because the graph is empty");
            alert.showAndWait();
        } else if (graph.size() == 1) {
            alert.setContentText("Can't add more edges \nbecause there is only one vertex");
            alert.showAndWait();
        } else {
            boolean added = false;
            while (!added) {
                int idxA = Utility.random(numbers.size()) - 1;
                int idxB = Utility.random(numbers.size()) - 1;
                Object a = numbers.get(idxA);
                Object b = numbers.get(idxB);
                if (!graph.containsEdge(a, b) && !a.equals(b)) {
                    int weight = Utility.random(49) + 1;
                    graph.addEdgeWeight(a, b, weight);
                    added = true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    // Auxiliar para obtener número aleatorio no repetido para nuevo vértice
    private int getUnusedRandomNumber() {
        ArrayList<Integer> pool = new ArrayList<>();
        for (int i = 0; i < 100; i++) pool.add(i);
        pool.removeAll(numbers);
        Collections.shuffle(pool);
        return pool.get(0);
    }
}
