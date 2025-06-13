module lab12.laboratory12 {
    requires javafx.controls;
    requires javafx.fxml;


    opens lab12.laboratory12 to javafx.fxml;
    exports lab12.laboratory12;
    exports controller;
    opens controller to javafx.fxml;
}