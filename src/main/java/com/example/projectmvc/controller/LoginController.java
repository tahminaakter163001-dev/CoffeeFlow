package com.example.projectmvc.controller;

import com.example.projectmvc.SessionManager;
import com.example.projectmvc.database.UserDAO;
import com.example.projectmvc.model.User;

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
import javafx.concurrent.Task;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin(ActionEvent event)
            throws Exception {

        String username =
                usernameField.getText().trim();

        String password =
                passwordField.getText();

        if (username.isEmpty()
                || password.isEmpty()) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Login");
            alert.setHeaderText(null);

            alert.setContentText(
                    "Please enter username and password."
            );

            alert.showAndWait();

            return;
        }

        // Background database task
        Task<User> loginTask =
                new Task<>() {

                    @Override
                    protected User call() {

                        return userDAO.loginUser(
                                username,
                                password
                        );
                    }
                };

        // Task completed successfully
        loginTask.setOnSucceeded(e -> {

            User user =
                    loginTask.getValue();

            if (user != null) {

                SessionManager.setUsername(
                        user.getUsername()
                );

                try {

                    if (user.getRole().equals("admin")) {

                        openPage(
                                event,
                                "/com/example/projectmvc/view/dashboard-view.fxml",
                                "CoffeeFlow - Admin Dashboard"
                        );

                    } else if (
                            user.getRole().equals("customer")) {

                        openPage(
                                event,
                                "/com/example/projectmvc/view/customer-dashboard-view.fxml",
                                "CoffeeFlow - Customer Dashboard"
                        );
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            } else {

                Alert alert =
                        new Alert(
                                Alert.AlertType.ERROR
                        );

                alert.setTitle("Login Failed");
                alert.setHeaderText(null);

                alert.setContentText(
                        "Invalid username or password."
                );

                alert.showAndWait();
            }
        });

        // Task failed unexpectedly
        loginTask.setOnFailed(e -> {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Login Error");
            alert.setHeaderText(null);

            alert.setContentText(
                    "An error occurred while logging in."
            );

            alert.showAndWait();
        });

        // Start background thread
        Thread loginThread =
                new Thread(loginTask);

        loginThread.setDaemon(true);

        loginThread.start();
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
    @FXML
    private void openRegister(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/register-view.fxml"
                        )
                );

        Parent registerPage =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(registerPage)
        );

        stage.setTitle(
                "CoffeeFlow - Create Account"
        );

        stage.show();
    }

}
