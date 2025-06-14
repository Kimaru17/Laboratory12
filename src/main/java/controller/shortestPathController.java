package controller;

import domain.*;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import util.Utility;

import java.util.Map;

public class shortestPathController {

    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TableColumn col_vertex;
    @javafx.fxml.FXML
    private TableColumn col_position;
    @javafx.fxml.FXML
    private TableView tableView;
    @javafx.fxml.FXML
    private TableColumn col_distance;
    @javafx.fxml.FXML
    private RadioButton rb_linkedList;
    @javafx.fxml.FXML
    private RadioButton rb_adjList;
    @javafx.fxml.FXML
    private RadioButton rb_adjMatrix;
    private AdjacencyListGraph graphALG;
    private AdjacencyMatrixGraph graphAMG;
    private SinglyLinkedListGraph graphSLLG;

    public void initialize() throws GraphException, ListException {
        if (rb_adjMatrix.isSelected())
            graphAMG = new AdjacencyMatrixGraph(10);
        else if (rb_adjList.isSelected())
            graphALG = new AdjacencyListGraph(10);
        else if (rb_linkedList.isSelected())
            graphSLLG = new SinglyLinkedListGraph();

        setGraph();
        generateEdges();
        displayGraph();

        //tableview
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        col_vertex.setCellValueFactory(new PropertyValueFactory<>("vertex"));
        col_distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
    }

    public void runDijkstra() throws GraphException, ListException {
        int startVertex = 0; // Ejemplo: empezar desde el vértice 0
        if (rb_adjMatrix.isSelected()) {
            int[] distances = graphAMG.dijkstra(startVertex);

            tableView.getItems().clear();
            for (int i = 0; i < distances.length; i++) {
                String distanceStr = (distances[i] == Integer.MAX_VALUE) ? "N/A" : String.valueOf(distances[i]);
                tableView.getItems().add(new TableData(i, graphAMG.getVertexByIndex(i).data, distanceStr));
            }
        } else if (rb_adjList.isSelected()){
            int[] distances = graphALG.dijkstra(startVertex);

            tableView.getItems().clear();
            for (int i = 0; i < distances.length; i++) {
                String distanceStr = (distances[i] == Integer.MAX_VALUE) ? "N/A" : String.valueOf(distances[i]);
                tableView.getItems().add(new TableData(i, graphALG.getVertexByIndex(i).data, distanceStr));
            }
        } else if (rb_linkedList.isSelected()) {

        }
    }

    private void setGraph() throws GraphException, ListException {
        int verterx = 0;
        if (rb_adjMatrix.isSelected()) {
            for (int i = 0; i < 10; i++) {
                verterx = Utility.getDifferentRandom(graphAMG, 99);
                graphAMG.addVertex(verterx);
            }
        } else if (rb_adjList.isSelected()) {
            for (int i = 0; i < 10; i++) {
                verterx = Utility.getDifferentRandom(graphALG, 99);
                graphALG.addVertex(verterx);
            }
        } else if (rb_linkedList.isSelected()) {
            int count = 1;
            while (count <= 10){
                int randomIndex = Utility.random(99);
                if (graphSLLG.isEmpty()) {
                    graphSLLG.addVertex(randomIndex);
                    count++;
                } else{
                    int currentInteger = Utility.random(99);
                    if (!graphSLLG.containsVertex(currentInteger)){
                        graphSLLG.addVertex(currentInteger);
                        count++;
                    }
                }
            }
        }
    }

    private void displayGraph() throws ListException, GraphException {
        if (rb_adjMatrix.isSelected()){
            AdjMatrixGraphVisualization graphVisualization = new AdjMatrixGraphVisualization(graphAMG);
            graphVisualization.displayGraph();
            paneGraph.getChildren().clear();
            paneGraph.getChildren().add(graphVisualization);
        } else if (rb_adjList.isSelected()){
            AdjListGraphVisualization graphVisualization = new AdjListGraphVisualization(graphALG);
            graphVisualization.displayGraph();
            paneGraph.getChildren().clear();
            paneGraph.getChildren().add(graphVisualization);
        } else if (rb_linkedList.isSelected()){
            SinglyLinkedListGraphVisualization visualization = new SinglyLinkedListGraphVisualization(graphSLLG);
            visualization.displayGraph();
            paneGraph.getChildren().clear();
            paneGraph.getChildren().add(visualization);
        }
    }

    private void generateEdges() throws GraphException, ListException {
        for (int i = 0; i < 15; i++) {
            Object a;
            Object b;
            int weight;

            if (rb_adjMatrix.isSelected()){
                a = graphAMG.getVertexByIndex(Utility.random(10)-1).data;
                b = graphAMG.getVertexByIndex(Utility.random(10)-1).data;
                weight = Utility.random(200) + 800;
                graphAMG.addEdgeWeight(a, b, weight);
            } else if (rb_adjList.isSelected()){
                a = graphALG.getVertexByIndex(Utility.random(10)-1).data;
                b = graphALG.getVertexByIndex(Utility.random(10)-1).data;
                weight = Utility.random(200) + 800;
                graphALG.addEdgeWeight(a, b, weight);
            } else if (rb_linkedList.isSelected()){
                a = graphSLLG.getVertexByIndex(Utility.random(10)).data;
                b = graphSLLG.getVertexByIndex(Utility.random(10)).data;
                weight = Utility.random(200) + 800;
                graphSLLG.addEdgeWeight(a, b, weight);
            }
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        graphAMG = new AdjacencyMatrixGraph(10);
        graphALG = new AdjacencyListGraph(10);
        graphSLLG = new SinglyLinkedListGraph();
        setGraph();
        generateEdges();
        displayGraph();

    }

    @javafx.fxml.FXML
    public void adjacencyMatrixOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_adjMatrix.isSelected()){
            rb_adjMatrix.setSelected(true);
            rb_adjList.setSelected(false);
            rb_linkedList.setSelected(false);

            initialize();
            runDijkstra();
        }
    }

    @javafx.fxml.FXML
    public void adjacencyListOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_adjList.isSelected()) {
            rb_adjMatrix.setSelected(false);
            rb_adjList.setSelected(true);
            rb_linkedList.setSelected(false);

            initialize();
            runDijkstra();
        }
    }

    @javafx.fxml.FXML
    public void linkedListOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_linkedList.isSelected()){
            rb_adjMatrix.setSelected(false);
            rb_adjList.setSelected(false);
            rb_linkedList.setSelected(true);

            initialize();
            runDijkstra();
        }
    }


}
