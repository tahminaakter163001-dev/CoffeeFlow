package com.example.projectmvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactController {

    @FXML
    private void handleBack(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/home-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource())
                    .getScene();

            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();

            stage.setTitle("CoffeeFlow");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}