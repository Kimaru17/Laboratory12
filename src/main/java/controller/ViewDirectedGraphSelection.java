package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ViewDirectedGraphSelection
{
    @javafx.fxml.FXML
    private AnchorPane containerPane;
    @javafx.fxml.FXML
    private RadioButton rb_LinkedList;
    @javafx.fxml.FXML
    private RadioButton rb_adjList;
    @javafx.fxml.FXML
    private RadioButton rb_adjMatrix;

    @javafx.fxml.FXML
    public void initialize() {
        // Opcional: Puedes dejar un radioButton seleccionado por defecto y mostrar esa vista inicial
        rb_adjMatrix.setSelected(false);
        rb_LinkedList.setSelected(false);
        rb_adjList.setSelected(false);
    }

    @javafx.fxml.FXML
    public void AdjacencyMatrixOnAction(ActionEvent actionEvent) {
        loadView("adjMatrixGraphView.fxml");
        rb_adjMatrix.setSelected(true);
        rb_LinkedList.setSelected(false);
        rb_adjList.setSelected(false);
    }

    @javafx.fxml.FXML
    public void AdjacencyListOnAction(ActionEvent actionEvent) {
        loadView("adjListGraphView.fxml");
        rb_adjMatrix.setSelected(false);
        rb_LinkedList.setSelected(false);
        rb_adjList.setSelected(true);
    }

    @javafx.fxml.FXML
    public void LinkedListOnAction(ActionEvent actionEvent) {
        loadView("directedSinglyLinkedListGraphView.fxml");
        rb_adjMatrix.setSelected(false);
        rb_LinkedList.setSelected(true);
        rb_adjList.setSelected(false);
    }
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab12/laboratory12/" + fxmlFile));
            Pane view = loader.load();
            containerPane.getChildren().setAll(view);
            // Opcional: ajustar el tamaño del hijo al contenedor
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}