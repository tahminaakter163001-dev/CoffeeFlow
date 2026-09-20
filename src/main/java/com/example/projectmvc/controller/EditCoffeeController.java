package com.example.projectmvc.controller;

import com.example.projectmvc.model.CoffeeMenuRow;
import com.example.projectmvc.database.CoffeeDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditCoffeeController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField smallPriceField;

    @FXML
    private TextField mediumPriceField;

    @FXML
    private TextField largePriceField;

    @FXML
    private Label messageLabel;

    private CoffeeMenuRow selectedCoffee;

    private final CoffeeDAO coffeeDAO = new CoffeeDAO();

    public void setSelectedCoffee(CoffeeMenuRow coffee) {

        selectedCoffee = coffee;

        nameField.setText(coffee.getName());

        smallPriceField.setText(
                String.valueOf(coffee.getSmallPrice())
        );

        mediumPriceField.setText(
                String.valueOf(coffee.getMediumPrice())
        );

        largePriceField.setText(
                String.valueOf(coffee.getLargePrice())
        );
    }

    @FXML
    private void handleUpdate(ActionEvent event) {

        if (selectedCoffee == null) {

            messageLabel.setText(
                    "No coffee selected."
            );

            return;
        }

        String oldName =
                selectedCoffee.getName();

        String newName =
                nameField.getText().trim();

        if (newName.isEmpty()) {

            messageLabel.setText(
                    "Coffee name cannot be empty."
            );

            return;
        }

        double smallPrice;
        double mediumPrice;
        double largePrice;

        try {

            smallPrice =
                    Double.parseDouble(
                            smallPriceField.getText().trim()
                    );

            mediumPrice =
                    Double.parseDouble(
                            mediumPriceField.getText().trim()
                    );

            largePrice =
                    Double.parseDouble(
                            largePriceField.getText().trim()
                    );

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Please enter valid prices."
            );

            return;
        }

        if (smallPrice < 0 ||
                mediumPrice < 0 ||
                largePrice < 0) {

            messageLabel.setText(
                    "Prices cannot be negative."
            );

            return;
        }

        // Background database task
        javafx.concurrent.Task<Boolean> updateTask =
                new javafx.concurrent.Task<>() {

                    @Override
                    protected Boolean call() {

                        return coffeeDAO.updateCoffee(
                                oldName,
                                newName,
                                smallPrice,
                                mediumPrice,
                                largePrice
                        );
                    }
                };

        // Task completed successfully
        updateTask.setOnSucceeded(e -> {

            boolean success =
                    updateTask.getValue();

            if (success) {

                messageLabel.setText(
                        "Coffee updated successfully!"
                );

                selectedCoffee = null;

            } else {

                messageLabel.setText(
                        "Failed to update coffee."
                );
            }
        });

        // Task failed unexpectedly
        updateTask.setOnFailed(e -> {

            messageLabel.setText(
                    "An error occurred while updating coffee."
            );
        });

        // Start background thread
        Thread updateThread =
                new Thread(updateTask);

        updateThread.setDaemon(true);

        updateThread.start();
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {

        javafx.fxml.FXMLLoader loader =
                new javafx.fxml.FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/menu-view.fxml"
                        )
                );

        javafx.scene.Parent menuPage = loader.load();

        javafx.stage.Stage stage =
                (javafx.stage.Stage)
                        ((javafx.scene.Node) event.getSource())
                                .getScene()
                                .getWindow();

        stage.setScene(
                new javafx.scene.Scene(menuPage)
        );

        stage.setTitle("CoffeeFlow - Coffee Menu");
        stage.show();
    }
}

