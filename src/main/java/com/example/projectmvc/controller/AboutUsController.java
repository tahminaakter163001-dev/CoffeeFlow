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

public class AboutUsController {

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

        AboutUs aboutUs = JsonService.getAboutUs();

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
    }

    @FXML
    private void handleBack(ActionEvent event) {

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