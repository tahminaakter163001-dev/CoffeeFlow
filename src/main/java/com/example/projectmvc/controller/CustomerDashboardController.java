package com.example.projectmvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerDashboardController {

    @FXML
    private void openOrder(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/order-view.fxml"
                )
        );

        Parent orderPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(orderPage));
        stage.setTitle("CoffeeFlow - New Order");
        stage.show();
    }


    @FXML
    private void openMyOrders(ActionEvent event) {

        System.out.println("My Orders clicked.");
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/login-view.fxml"
                )
        );

        Parent loginPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(loginPage));
        stage.setTitle("CoffeeFlow - Login");
        stage.show();
    }
}

