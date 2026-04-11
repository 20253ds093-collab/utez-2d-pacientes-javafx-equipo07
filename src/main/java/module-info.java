module com.example.integradora {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.integradora.Models to javafx.base;
    opens com.example.integradora to javafx.fxml;
    exports com.example.integradora;
    exports com.example.integradora.Controllers;
    opens com.example.integradora.Controllers to javafx.fxml;
}