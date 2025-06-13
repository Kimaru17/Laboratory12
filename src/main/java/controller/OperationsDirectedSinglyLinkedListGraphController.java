package controller;

import domain.DirectedSinglyLinkedListGraph;
import domain.GraphException;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import util.Utility;
import util.FXUtility;

public class OperationsDirectedSinglyLinkedListGraphController
{
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private TextArea ta_toString;

    private String[] monuments;
    DirectedSinglyLinkedListGraph graph;
    private Alert alert;
    DirectedSinglyLinkedListGraphVisualization visualization;

    @javafx.fxml.FXML
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
        ta_toString.setVisible(true);
        ta_toString.setText(graph.toString());
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
            String currentMonument = monuments[randomIndex];
            graph.addVertex(currentMonument);
            displayGraph();
            ta_toString.setVisible(true);
            ta_toString.setText(graph.toString());
            System.out.println("pase");
        }else if (graph.size()==10) {
            alert.setContentText("The graph is full");
            alert.showAndWait();
        } else {
            boolean added = false;
            while (!added) {
                int randomIndex = util.Utility.random(30) - 1;
                String currentMonument = monuments[randomIndex];
                if (!graph.containsVertex(currentMonument)) {
                    graph.addVertex(currentMonument);
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
                int randomIndex = util.Utility.random(30) - 1;
                String currentMonuments = monuments[randomIndex];
                if (graph.containsVertex(currentMonuments)) {
                    graph.removeVertex(currentMonuments);
                    removed= true;
                }
            }
            displayGraph();
            ta_toString.setText(graph.toString());
        }
    }
    @javafx.fxml.FXML
    public void addEdgesAndWeightsOnAction(ActionEvent actionEvent) throws GraphException, ListException {
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
                    int weight = util.Utility.random(200) + 200;
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