package com.example.projectmvc.controller;

import com.example.projectmvc.database.CoffeeDAO;
import com.example.projectmvc.model.Coffee;

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

    private final CoffeeDAO coffeeDAO = new CoffeeDAO();

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

        String name = nameField.getText().trim();
        String size = sizeComboBox.getValue();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() ||
                size == null ||
                priceText.isEmpty()) {

            messageLabel.setText(
                    "Please fill in all fields."
            );

            return;
        }

        try {

            double price = Double.parseDouble(priceText);

            if (price <= 0) {

                messageLabel.setText(
                        "Price must be greater than zero."
                );

                return;
            }

            Coffee coffee =
                    new Coffee(0, name, size, price);

            coffeeDAO.addCoffee(coffee);

            messageLabel.setText(
                    "Coffee added successfully!"
            );

            nameField.clear();
            priceField.clear();
            sizeComboBox.setValue(null);

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Please enter a valid price."
            );
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/menu-view.fxml"
                )
        );

        Parent menuPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(menuPage));
        stage.setTitle("CoffeeFlow - Coffee Menu");
        stage.show();
    }
}
