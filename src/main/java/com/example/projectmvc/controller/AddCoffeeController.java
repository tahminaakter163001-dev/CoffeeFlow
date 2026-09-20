package com.example.projectmvc.controller;

import com.example.projectmvc.database.CoffeeDAO;
import com.example.projectmvc.model.Coffee;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCoffeeController {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> sizeComboBox;

    @FXML
    private TextField priceField;

    @FXML
    private Label messageLabel;

    private final CoffeeDAO coffeeDAO =
            new CoffeeDAO();

    @FXML
    private void initialize() {

        sizeComboBox.getItems().addAll(
                "Small",
                "Medium",
                "Large"
        );
    }

    @FXML
    private void handleAddCoffee(ActionEvent event) {

        String name =
                nameField.getText().trim();

        String size =
                sizeComboBox.getValue();

        String priceText =
                priceField.getText().trim();

        // Validation
        if (name.isEmpty()
                || size == null
                || priceText.isEmpty()) {

            messageLabel.setText(
                    "Please fill in all fields."
            );

            return;
        }

        double price;

        try {

            price =
                    Double.parseDouble(priceText);

            if (price <= 0) {

                messageLabel.setText(
                        "Price must be greater than zero."
                );

                return;
            }

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Please enter a valid price."
            );

            return;
        }

        Coffee coffee =
                new Coffee(
                        0,
                        name,
                        size,
                        price
                );

        // Background database task
        Task<Boolean> addTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return coffeeDAO.addCoffee(
                                coffee
                        );
                    }
                };

        // Task completed successfully
        addTask.setOnSucceeded(e -> {

            boolean success =
                    addTask.getValue();

            if (success) {

                messageLabel.setText(
                        "Coffee added successfully!"
                );

                nameField.clear();
                priceField.clear();
                sizeComboBox.setValue(null);

            } else {

                messageLabel.setText(
                        "Failed to add coffee."
                );
            }
        });

        // Task failed unexpectedly
        addTask.setOnFailed(e -> {

            messageLabel.setText(
                    "An error occurred while adding coffee."
            );
        });

        // Start background thread
        Thread addThread =
                new Thread(addTask);

        addThread.setDaemon(true);

        addThread.start();
    }

    @FXML
    private void goBack(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/menu-view.fxml"
                        )
                );

        Parent menuPage =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(menuPage)
        );

        stage.setTitle(
                "CoffeeFlow - Coffee Menu"
        );

        stage.show();
    }
}