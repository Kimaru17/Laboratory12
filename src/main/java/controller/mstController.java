package controller;

import domain.*;
import domain.list.ListException;
import domain.list.SinglyLinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import util.Utility;

import java.util.ArrayList;
import java.util.List;

public class mstController {

    @FXML
    private Pane paneGraphLeft;
    @FXML
    private Pane paneGraphRight;
    @FXML
    private RadioButton rb_kruskal;
    @FXML
    private RadioButton rb_prim;
    @FXML
    private RadioButton rb_linkedList;
    @FXML
    private RadioButton rb_adjList;
    @FXML
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
    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        graphAMG = new AdjacencyMatrixGraph(10);
        graphALG = new AdjacencyListGraph(10);
        graphSLLG = new SinglyLinkedListGraph();
        setGraph();
        generateEdges();
        displayGraph();
        rb_prim.setSelected(false);
        rb_kruskal.setSelected(false);
        paneGraphRight.setVisible(false);
    }

    @FXML
    public void adjacencyMatrixOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_adjMatrix.isSelected()){
            rb_adjMatrix.setSelected(true);
            rb_adjList.setSelected(false);
            rb_linkedList.setSelected(false);
            rb_prim.setSelected(false);
            rb_kruskal.setSelected(false);
            initialize();
        }
    }

    @FXML
    public void adjacencyListOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_adjList.isSelected()) {
            rb_adjMatrix.setSelected(false);
            rb_adjList.setSelected(true);
            rb_linkedList.setSelected(false);
            rb_prim.setSelected(false);
            rb_kruskal.setSelected(false);

            initialize();
        }
    }

    @FXML
    public void linkedListOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_linkedList.isSelected()){
            rb_adjMatrix.setSelected(false);
            rb_adjList.setSelected(false);
            rb_linkedList.setSelected(true);
            rb_prim.setSelected(false);
            rb_kruskal.setSelected(false);

            initialize();
        }
    }

    ////////////////////////////////////////////////////////////

   @FXML
    public void kruskalOnAction(ActionEvent actionEvent) throws GraphException, ListException {
       if (rb_adjList.isSelected()) {
           AdjacencyListGraph mst = graphALG.kruskalMST();
           AdjListGraphVisualization vis = new AdjListGraphVisualization(mst);
           vis.displayGraph();
           paneGraphRight.getChildren().clear();
           paneGraphRight.getChildren().add(vis);
       } else if (rb_adjMatrix.isSelected()) {
           AdjacencyMatrixGraph mst = graphAMG.kruskalMST();
           AdjMatrixGraphVisualization vis = new AdjMatrixGraphVisualization(mst);
           vis.displayGraph();
           paneGraphRight.getChildren().clear();
           paneGraphRight.getChildren().add(vis);
       } else if (rb_linkedList.isSelected()) {
           SinglyLinkedListGraph mst = graphSLLG.kruskalMST();
           SinglyLinkedListGraphVisualization vis = new SinglyLinkedListGraphVisualization(mst);
           vis.displayGraph();
           paneGraphRight.getChildren().clear();
           paneGraphRight.getChildren().add(vis);
       }
       rb_prim.setSelected(false);
       rb_kruskal.setSelected(true);
       paneGraphRight.setVisible(true);
    }

    @FXML
    public void primOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        if (rb_adjList.isSelected()) {
            AdjacencyListGraph mst = graphALG.primMST();
            AdjListGraphVisualization vis = new AdjListGraphVisualization(mst);
            vis.displayGraph();
            paneGraphRight.getChildren().clear();
            paneGraphRight.getChildren().add(vis);
        } else if (rb_adjMatrix.isSelected()) {
            AdjacencyMatrixGraph mst = graphAMG.primMST();
            AdjMatrixGraphVisualization vis = new AdjMatrixGraphVisualization(mst);
            vis.displayGraph();
            paneGraphRight.getChildren().clear();
            paneGraphRight.getChildren().add(vis);
        } else if (rb_linkedList.isSelected()) {
            SinglyLinkedListGraph mst = graphSLLG.primMST();
            SinglyLinkedListGraphVisualization vis = new SinglyLinkedListGraphVisualization(mst);
            vis.displayGraph();
            paneGraphRight.getChildren().clear();
            paneGraphRight.getChildren().add(vis);
        }
        rb_prim.setSelected(true);
        rb_kruskal.setSelected(false);
        paneGraphRight.setVisible(true);
    }

}
