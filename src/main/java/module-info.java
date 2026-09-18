module com.example.projectmvc {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.projectmvc to javafx.fxml;
    opens com.example.projectmvc.controller to javafx.fxml;

    exports com.example.projectmvc;
}

