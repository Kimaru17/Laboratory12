package controller;

import domain.GraphException;
import domain.DirectedSinglyLinkedListGraph;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import util.Utility;
import util.FXUtility;

public class OperationsDirectedSinglyLinkedListGraphController
{
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TextArea ta_toString;

    private String[] names;
    DirectedSinglyLinkedListGraph graph;
    DirectedSinglyLinkedListGraphVisualization visualization;
    private Alert alert;

    @javafx.fxml.FXML
    public void initialize() throws GraphException, ListException {
        names= new String[] {"Jesús", "Mahoma", "C.Colón", "J. Cesar", "A. Magno",
                "Platón", "Lao Tse", "L. Da Vinci", "Gutenberg", "A. Einstein",
                "C. Darwin", "Karl Marx", "I. Newton", "M. Gandhi", "L. Pasteur",
                "G. Galilei", "Mozart", "Aristóteles", "M. Ángel", "Amstrong",
                "Stalin", "M. Curie", "M. Luther King", "Napoleón", "M. Lutero",
                "Shakespeare", "Pitágoras", "G. Washington", "Lenin", "Buda",
                "Confucio", "Euclides", "Constantino I", "Isabel I", "F. Pizarro",
                "H. Cortés", "J. Calvino", "Beethoven", "N. Copérnico", "Homero",
                "Pedro I", "Cleopatra", "Edison", "N. Tesla", "B. Gates",
                "A. Fleming", "S. de Beauvoir", "J. Watt", "Tales", "J. Dalton"};

        graph= new DirectedSinglyLinkedListGraph();
        alert = FXUtility.alert("Operations DirectedSinglyLinkedListGraph", null);

        setGraph();
        generateEdges();
        displayGraph();
        ta_toString.setVisible(true);
        ta_toString.setText(graph.toString());

    }

    private void setGraph() throws GraphException, ListException {
        int count = 1;
        while (count <= 10){
            int randomIndex = util.Utility.random(50)-1;
            if (graph.isEmpty()) {
                graph.addVertex(names[randomIndex]);
                count++;
            } else{
                String currentName = names[randomIndex];
                if (!graph.containsVertex(currentName)){
                    graph.addVertex(currentName);
                    count++;
                }
            }
        }
    }
    private void generateEdges() throws GraphException, ListException {
        for (int i = 0; i < 15; i++) {
            Object a = graph.getVertexByIndex(Utility.random(10)).data;
            Object b = graph.getVertexByIndex(Utility.random(10)).data;

            int weight = util.Utility.random(1000) + 1000;

            graph.addEdgeWeight(a, b, weight);
        }
    }

    private void displayGraph() throws ListException, GraphException {
        paneGraph.getChildren().clear();
        visualization = new DirectedSinglyLinkedListGraphVisualization(graph);
        visualization.displayGraph();
        paneGraph.getChildren().add(visualization);
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) throws GraphException, ListException {
        graph = new DirectedSinglyLinkedListGraph();
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
            int randomIndex = util.Utility.random(50) - 1;
            String currentName = names[randomIndex];
            graph.addVertex(currentName);
            displayGraph();
            ta_toString.setVisible(true);
            ta_toString.setText(graph.toString());
        }else if (graph.size()==10) {
            alert.setContentText("The graph is full");
            alert.showAndWait();
        } else {
            boolean added = false;
            while (!added) {
                int randomIndex = util.Utility.random(50) - 1;
                String currentName = names[randomIndex];
                if (!graph.containsVertex(currentName)) {
                    graph.addVertex(currentName);
                    added= true;
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
            alert.setContentText("Cant remove more vertex \nbecause graph is empty");
            alert.showAndWait();
        }else if(graph.size()==1){
            graph.clear();
            ta_toString.setVisible(false);
            ta_toString.setText("");
            paneGraph.getChildren().clear();
            alert.setContentText("Cant remove more vertex \nbecause graph is empty");
            alert.showAndWait();
        }
        else {
            boolean removed = false;
            while (!removed) {
                int randomIndex = util.Utility.random(50) - 1;
                String currentName = names[randomIndex];
                if (graph.containsVertex(currentName)) {
                    graph.removeVertex(currentName);
                    removed= true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void addEdgesAndWeightsOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()){
            alert.setContentText("Cant add edges and weight because the graph is empty");
            alert.showAndWait();
        }else if(graph.size()==1){
            alert.setContentText("Cant add more edges \nbecause there is only one vertex");
            alert.showAndWait();
        }else {
            boolean added = false;
            while (!added) {
                Object a = graph.getVertexByIndex(Utility.random(graph.size())).data;
                Object b = graph.getVertexByIndex(Utility.random(graph.size())).data;
                if (!graph.containsEdge(a,b) && a!=b) {
                    int weight = util.Utility.random(1000) + 1000;
                    graph.addEdgeWeight(a, b, weight);
                    added=true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }

    @javafx.fxml.FXML
    public void RemoveEdgesAndWeightsOnAction(ActionEvent actionEvent) throws ListException, GraphException {
        if (graph.isEmpty()) {
            alert.setContentText("Cant remove more edges \nbecause graph is empty");
            alert.showAndWait();
        }else if(graph.size()==1){
            alert.setContentText("Cant remove more edges \nbecause there is only one vertex");
            alert.showAndWait();
        }
        else {
            boolean removed = false;
            while (!removed) {
                Object a = graph.getVertexByIndex(Utility.random(graph.size())).data;
                Object b = graph.getVertexByIndex(Utility.random(graph.size())).data;
                if (graph.containsEdge(a,b)) {
                    graph.removeEdge(a, b);
                    removed=true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }
}
