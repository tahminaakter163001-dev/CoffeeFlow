package com.example.projectmvc.controller;

import com.example.projectmvc.database.UserDAO;
import com.example.projectmvc.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() {

        String username =
                usernameField.getText().trim();

        String password =
                passwordField.getText();

        String confirmPassword =
                confirmPasswordField.getText();

        if (username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            messageLabel.setText(
                    "Please fill in all fields."
            );

            return;
        }

        if (!password.equals(confirmPassword)) {

            messageLabel.setText(
                    "Passwords do not match."
            );

            return;
        }

        if (userDAO.usernameExists(username)) {

            messageLabel.setText(
                    "Username already exists."
            );

            return;
        }

        User user =
                new User(
                        username,
                        password,
                        "customer"
                );

        boolean registered =
                userDAO.registerUser(user);

        if (registered) {

            messageLabel.setText(
                    "Account created successfully!"
            );

            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();

        } else {

            messageLabel.setText(
                    "Registration failed."
            );
        }
    }

    @FXML
    private void goBack(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/login-view.fxml"
                        )
                );

        Parent loginPage =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(loginPage)
        );

        stage.setTitle(
                "CoffeeFlow - Login"
        );

        stage.show();
    }
}


