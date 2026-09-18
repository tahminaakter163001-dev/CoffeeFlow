package com.example.projectmvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.projectmvc.database.DatabaseInitializer;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        DatabaseInitializer.createTables();

        FXMLLoader fxmlLoader =
                new FXMLLoader(Main.class.getResource("/com/example/projectmvc/view/login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("CoffeeFlow");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
