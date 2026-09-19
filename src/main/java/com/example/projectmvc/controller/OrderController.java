package com.example.projectmvc.controller;

import com.example.projectmvc.database.CoffeeDAO;
import com.example.projectmvc.model.CoffeeMenuRow;
import com.example.projectmvc.model.OrderItem;
import com.example.projectmvc.database.OrderDAO;
import com.example.projectmvc.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class OrderController {

    @FXML
    private ComboBox<String> coffeeComboBox;

    @FXML
    private ComboBox<String> sizeComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<OrderItem> cartTable;

    @FXML
    private TableColumn<OrderItem, String> coffeeColumn;

    @FXML
    private TableColumn<OrderItem, String> sizeColumn;

    @FXML
    private TableColumn<OrderItem, Number> quantityColumn;

    @FXML
    private TableColumn<OrderItem, Number> priceColumn;

    @FXML
    private TableColumn<OrderItem, Number> totalColumn;

    @FXML
    private Label totalLabel;

    private final CoffeeDAO coffeeDAO = new CoffeeDAO();

    private final ObservableList<OrderItem> cart =
            FXCollections.observableArrayList();

    private ObservableList<CoffeeMenuRow> coffeeList;

    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    private void initialize() {

        // TableView columns
        coffeeColumn.setCellValueFactory(
                data -> data.getValue().coffeeNameProperty()
        );

        sizeColumn.setCellValueFactory(
                data -> data.getValue().sizeProperty()
        );

        quantityColumn.setCellValueFactory(
                data -> data.getValue().quantityProperty()
        );

        priceColumn.setCellValueFactory(
                data -> data.getValue().priceProperty()
        );

        totalColumn.setCellValueFactory(
                data -> data.getValue().totalProperty()
        );

        cartTable.setItems(cart);

        // Load coffee names
        coffeeList =
                coffeeDAO.getAllCoffeeMenuRows();

        for (CoffeeMenuRow coffee : coffeeList) {
            coffeeComboBox.getItems().add(
                    coffee.getName()
            );
        }

        // Available sizes
        sizeComboBox.getItems().addAll(
                "Small",
                "Medium",
                "Large"
        );
    }


    @FXML
    private void handleAddToCart(ActionEvent event) {

        String coffeeName =
                coffeeComboBox.getValue();

        String size =
                sizeComboBox.getValue();

        String quantityText =
                quantityField.getText().trim();


        if (coffeeName == null ||
                size == null ||
                quantityText.isEmpty()) {

            showMessage(
                    "Please select coffee, size and quantity."
            );

            return;
        }


        try {

            int quantity =
                    Integer.parseInt(quantityText);

            if (quantity <= 0) {

                showMessage(
                        "Quantity must be greater than zero."
                );

                return;
            }


            double price =
                    getPrice(coffeeName, size);


            if (price <= 0) {

                showMessage(
                        "This size is not available."
                );

                return;
            }


            OrderItem item =
                    new OrderItem(
                            coffeeName,
                            size,
                            quantity,
                            price
                    );

            cart.add(item);

            updateTotal();

            quantityField.clear();

        } catch (NumberFormatException e) {

            showMessage(
                    "Please enter a valid quantity."
            );
        }
    }


    private double getPrice(
            String coffeeName,
            String size) {

        for (CoffeeMenuRow coffee : coffeeList) {

            if (coffee.getName()
                    .equals(coffeeName)) {

                if (size.equals("Small")) {
                    return coffee.getSmallPrice();
                }

                if (size.equals("Medium")) {
                    return coffee.getMediumPrice();
                }

                if (size.equals("Large")) {
                    return coffee.getLargePrice();
                }
            }
        }

        return 0;
    }


    private void updateTotal() {

        double total = 0;

        for (OrderItem item : cart) {
            total += item.getTotal();
        }

        totalLabel.setText(
                String.format("%.2f", total)
        );
    }


    private void showMessage(String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("CoffeeFlow");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    @FXML
    private void handlePlaceOrder(ActionEvent event) {

        if (cart.isEmpty()) {

            showMessage(
                    "Your cart is empty."
            );

            return;
        }

        String customerName = SessionManager.getUsername();

        for (OrderItem item : cart) {

            orderDAO.saveOrder(
                    customerName,
                    item
            );
        }

        showMessage(
                "Order placed successfully!"
        );

        cart.clear();
        updateTotal();
    }

    @FXML
    private void goBack(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/customer-dashboard-view.fxml"
                        )
                );

        Parent customerDashboard =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(customerDashboard)
        );

        stage.setTitle(
                "CoffeeFlow - Customer Dashboard"
        );

        stage.show();
    }

}

