package com.example.projectmvc.controller;

import com.example.projectmvc.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event)
            throws Exception {

        String username =
                usernameField.getText().trim();

        String password =
                passwordField.getText().trim();

        // ADMIN LOGIN
        if (username.equals("admin")
                && password.equals("1234")) {

            SessionManager.setUsername(username);

            openPage(
                    event,
                    "/com/example/projectmvc/view/dashboard-view.fxml",
                    "CoffeeFlow - Admin Dashboard"
            );
        }

        // CUSTOMER LOGIN
        else if (username.equals("customer")
                && password.equals("1234")) {

            SessionManager.setUsername(username);

            openPage(
                    event,
                    "/com/example/projectmvc/view/customer-dashboard-view.fxml",
                    "CoffeeFlow - Customer Dashboard"
            );
        }

        // INVALID LOGIN
        else {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Login Failed");
            alert.setHeaderText(null);

            alert.setContentText(
                    "Invalid username or password."
            );

            alert.showAndWait();
        }
    }

    private void openPage(
            ActionEvent event,
            String fxmlPath,
            String title)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(fxmlPath)
                );

        Parent page = loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(page)
        );

        stage.setTitle(title);
        stage.show();
    }
}
