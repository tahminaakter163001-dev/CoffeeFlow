package com.example.projectmvc.controller;
import com.example.projectmvc.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private void logout(ActionEvent event) throws Exception {

        SessionManager.clearSession();
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
