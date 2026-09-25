package com.example.projectmvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @javafx.fxml.FXML
    private void handleHome(ActionEvent event) {

        System.out.println("Home clicked");
    }

    @FXML
    private void handleAboutUs(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/about-us-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("CoffeeFlow - About Us");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenu(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/customer-menu-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("CoffeeFlow - Menu");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    private void handleContact(ActionEvent event) {

        System.out.println("Contact clicked");
    }

    @javafx.fxml.FXML
    private void handleOrderNow(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/login-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("CoffeeFlow");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
