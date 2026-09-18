package com.example.projectmvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    public void openCoffeeMenu(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/menu-view.fxml"
                )
        );

        Parent menuPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        Scene scene = new Scene(menuPage);

        stage.setScene(scene);
        stage.setTitle("CoffeeFlow - Coffee Menu");
        stage.show();
    }
}
