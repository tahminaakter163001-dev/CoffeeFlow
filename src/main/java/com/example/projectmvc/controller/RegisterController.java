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
import javafx.concurrent.Task;

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

        // Validation
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

        User user =
                new User(
                        username,
                        password,
                        "customer"
                );

        // Background database task
        Task<Boolean> registerTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        // Check username in database
                        if (userDAO.usernameExists(username)) {

                            return false;
                        }

                        // Insert new user
                        return userDAO.registerUser(user);
                    }
                };

        // Task completed successfully
        registerTask.setOnSucceeded(e -> {

            boolean registered =
                    registerTask.getValue();

            if (registered) {

                messageLabel.setText(
                        "Account created successfully!"
                );

                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();

            } else {

                messageLabel.setText(
                        "Username already exists or registration failed."
                );
            }
        });

        // Task failed unexpectedly
        registerTask.setOnFailed(e -> {

            messageLabel.setText(
                    "An error occurred during registration."
            );
        });

        // Start background thread
        Thread registerThread =
                new Thread(registerTask);

        registerThread.setDaemon(true);

        registerThread.start();
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


