package com.example.projectmvc.controller;

import com.example.projectmvc.database.OrderDAO;
import com.example.projectmvc.model.OrderItem;
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
import javafx.concurrent.Task;

public class BillingController {

    @FXML
    private TableView<OrderItem> billingTable;

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

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    private Button confirmPaymentButton;

    private final OrderDAO orderDAO =
            new OrderDAO();

    private ObservableList<OrderItem> orderItems =
            FXCollections.observableArrayList();

    private double totalAmount;


    @FXML
    private void initialize() {

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

        billingTable.setItems(orderItems);

        paymentMethodComboBox.getItems().addAll(
                "Cash",
                "Card",
                "Mobile Banking"
        );
    }


    public void setOrderItems(
            ObservableList<OrderItem> items) {

        orderItems.clear();

        orderItems.addAll(items);

        calculateTotal();
    }


    private void calculateTotal() {

        totalAmount = 0;

        for (OrderItem item : orderItems) {

            totalAmount += item.getTotal();
        }

        totalLabel.setText(
                String.format(
                        "%.2f",
                        totalAmount
                )
        );
    }


    @FXML
    private void handleConfirmPayment(
            ActionEvent event) {

        if (orderItems.isEmpty()) {

            showMessage(
                    "There are no items in this bill."
            );

            return;
        }


        String paymentMethod =
                paymentMethodComboBox.getValue();


        if (paymentMethod == null) {

            showMessage(
                    "Please select a payment method."
            );

            return;
        }


        String customerName =
                SessionManager.getUsername();


        confirmPaymentButton.setDisable(true);


        ObservableList<OrderItem> itemsToSave =
                FXCollections.observableArrayList(
                        orderItems
                );


        Task<Boolean> paymentTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return orderDAO.saveBillWithOrders(
                                customerName,
                                totalAmount,
                                paymentMethod,
                                itemsToSave
                        );
                    }
                };


        paymentTask.setOnSucceeded(e -> {

            confirmPaymentButton.setDisable(false);


            boolean success =
                    paymentTask.getValue();


            if (success) {

                showMessage(
                        "Payment successful!\n\n"
                                + "Total: Tk "
                                + String.format(
                                "%.2f",
                                totalAmount
                        )
                                + "\nPayment Method: "
                                + paymentMethod
                );


                goToCustomerDashboard(event);

            } else {

                showMessage(
                        "Payment failed. "
                                + "Please try again."
                );
            }
        });


        paymentTask.setOnFailed(e -> {

            confirmPaymentButton.setDisable(false);

            showMessage(
                    "An error occurred "
                            + "while processing payment."
            );
        });


        Thread paymentThread =
                new Thread(paymentTask);

        paymentThread.setDaemon(true);

        paymentThread.start();
    }


    private void goToCustomerDashboard(
            ActionEvent event) {

        try {

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

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @FXML
    private void goBack(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/order-view.fxml"
                        )
                );


        Parent orderPage =
                loader.load();


        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();


        stage.setScene(
                new Scene(orderPage)
        );


        stage.setTitle(
                "CoffeeFlow - New Order"
        );


        stage.show();
    }


    private void showMessage(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle("CoffeeFlow");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}