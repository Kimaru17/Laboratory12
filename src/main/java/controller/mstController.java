package controller;

import domain.*;
import domain.list.ListException;
import domain.list.SinglyLinkedList;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import util.Utility;

import java.util.ArrayList;
import java.util.List;

public class mstController {

    @javafx.fxml.FXML
    private Pane paneGraphLeft;
    @javafx.fxml.FXML
    private Pane paneGraphRight;
    @javafx.fxml.FXML
    private RadioButton rb_kruskal;
    @javafx.fxml.FXML
    private RadioButton rb_prim;
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
    }

    private void setGraph() throws GraphException, ListException {
        if (rb_adjMatrix.isSelected())
            for (int i = 0; i < 10; i++)
                graphAMG.addVertex(Utility.getDifferentRandom(graphAMG, 99));
        else if (rb_adjList.isSelected())
            for (int i = 0; i < 10; i++)
                graphALG.addVertex(Utility.getDifferentRandom(graphALG, 99));
        else if (rb_linkedList.isSelected()) {
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
            paneGraphLeft.getChildren().clear();
            paneGraphLeft.getChildren().add(graphVisualization);
        } else if (rb_adjList.isSelected()){
            AdjListGraphVisualization graphVisualization = new AdjListGraphVisualization(graphALG);
            graphVisualization.displayGraph();
            paneGraphLeft.getChildren().clear();
            paneGraphLeft.getChildren().add(graphVisualization);
        } else if (rb_linkedList.isSelected()){
            SinglyLinkedListGraphVisualization visualization = new SinglyLinkedListGraphVisualization(graphSLLG);
            visualization.displayGraph();
            paneGraphLeft.getChildren().clear();
            paneGraphLeft.getChildren().add(visualization);
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
        }
    }

    @javafx.fxml.FXML
    public void adjacencyListOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_adjList.isSelected()) {
            rb_adjMatrix.setSelected(false);
            rb_adjList.setSelected(true);
            rb_linkedList.setSelected(false);

            initialize();
        }
    }

    @javafx.fxml.FXML
    public void linkedListOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_linkedList.isSelected()){
            rb_adjMatrix.setSelected(false);
            rb_adjList.setSelected(false);
            rb_linkedList.setSelected(true);

            initialize();
        }
    }

    ////////////////////////////////////////////////////////////

   @javafx.fxml.FXML
    public void kruskalOnAction(ActionEvent actionEvent) throws GraphException, ListException {

    }

    @javafx.fxml.FXML
    public void primOnAction(ActionEvent actionEvent) throws GraphException, ListException {

    }
}
