module com.example.projectmvc {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.projectmvc to javafx.fxml;
    opens com.example.projectmvc.controller to javafx.fxml;
    opens com.example.projectmvc.model to com.google.gson;

    exports com.example.projectmvc;
}

