package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import lab12.laboratory12.HelloApplication;

import java.io.IOException;

public class HelloController {

    @FXML
    private Text messageText;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void KruskalPrimOnAction(ActionEvent actionEvent) {
        loadPage("mstView.fxml");
    }

    @FXML
    public void ShortestPathOnAction(ActionEvent actionEvent) {
        loadPage("shortestPathView.fxml");
    }

    @FXML
    public void DirectedGraphOperationsOnAction(ActionEvent actionEvent) {
        loadPage("OperationDirectedGraphSelection.fxml");
    }

    @FXML
    public void DirectedGraphOnAction(ActionEvent actionEvent) {
        loadPage("ViewDirectedGraphSelection.fxml");
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        this.messageText.setText("Laboratory No.12");
        this.bp.setCenter(ap);
    }
}