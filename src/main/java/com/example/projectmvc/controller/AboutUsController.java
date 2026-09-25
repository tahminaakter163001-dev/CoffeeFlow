package com.example.projectmvc.controller;

import com.example.projectmvc.model.AboutUs;
import com.example.projectmvc.service.JsonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AboutUsController {
    private final ExecutorService executor =
            Executors.newFixedThreadPool(2, runnable -> {
                Thread thread = new Thread(runnable, "coffeeflow-json-worker");
                thread.setDaemon(true);
                return thread;
            });

    @FXML
    private javafx.scene.control.Label titleLabel;

    @FXML
    private javafx.scene.control.Label descriptionLabel;

    @FXML
    private javafx.scene.control.Label versionLabel;

    @FXML
    private javafx.scene.control.Label contactLabel;

    @FXML
    public void initialize() {

        javafx.concurrent.Task<AboutUs> task =
                new javafx.concurrent.Task<>() {

                    @Override
                    protected AboutUs call() throws Exception {
                        return JsonService.getAboutUs();
                    }
                };

        task.setOnSucceeded(event -> {

            AboutUs aboutUs = task.getValue();

            if (aboutUs != null) {

                titleLabel.setText(aboutUs.getTitle());
                descriptionLabel.setText(aboutUs.getDescription());
                versionLabel.setText("Version: " + aboutUs.getVersion());
                contactLabel.setText("Contact: " + aboutUs.getContact());

            } else {

                titleLabel.setText("About CoffeeFlow");
                descriptionLabel.setText(
                        "Unable to load About Us information."
                );
            }
        });

        task.setOnFailed(event -> {

            titleLabel.setText("About CoffeeFlow");
            descriptionLabel.setText(
                    "Unable to load About Us information."
            );

            task.getException().printStackTrace();
        });

        executor.execute(task);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        executor.shutdownNow();

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/login-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource())
                    .getScene();

            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();

            stage.setTitle("CoffeeFlow");

            stage.setWidth(900);
            stage.setHeight(600);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}