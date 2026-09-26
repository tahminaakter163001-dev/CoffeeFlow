package com.example.projectmvc.controller;

import com.example.projectmvc.SessionManager;
import com.example.projectmvc.database.OrderDAO;
import com.example.projectmvc.model.AdminOrder;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class CashierDashboardController {

    @FXML
    private TableView<AdminOrder> orderTable;

    @FXML
    private TableColumn<AdminOrder, Number> idColumn;

    @FXML
    private TableColumn<AdminOrder, String> customerColumn;

    @FXML
    private TableColumn<AdminOrder, String> coffeeColumn;

    @FXML
    private TableColumn<AdminOrder, String> sizeColumn;

    @FXML
    private TableColumn<AdminOrder, Number> quantityColumn;

    @FXML
    private TableColumn<AdminOrder, Number> priceColumn;

    @FXML
    private TableColumn<AdminOrder, Number> totalColumn;

    @FXML
    private TableColumn<AdminOrder, String> dateColumn;

    @FXML
    private TableColumn<AdminOrder, String> statusColumn;

    @FXML
    private Button confirmButton;

    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                data -> data.getValue().idProperty()
        );

        customerColumn.setCellValueFactory(
                data -> data.getValue().customerNameProperty()
        );

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

        dateColumn.setCellValueFactory(
                data -> data.getValue().orderDateProperty()
        );

        statusColumn.setCellValueFactory(
                data -> data.getValue().statusProperty()
        );

        confirmButton.setDisable(true);

        orderTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldOrder, newOrder) -> {

                    if (newOrder == null) {

                        confirmButton.setDisable(true);

                    } else {

                        confirmButton.setDisable(
                                !"Pending".equals(newOrder.getStatus())
                        );
                    }
                });

        loadOrders();
    }

    private void loadOrders() {

        confirmButton.setDisable(true);

        Task<ObservableList<AdminOrder>> orderTask =
                new Task<>() {

                    @Override
                    protected ObservableList<AdminOrder> call() {

                        return orderDAO.getCashierOrders();
                    }
                };

        orderTask.setOnSucceeded(e -> {

            ObservableList<AdminOrder> orders =
                    orderTask.getValue();

            orderTable.setItems(orders);

            confirmButton.setDisable(true);
        });

        orderTask.setOnFailed(e -> {

            confirmButton.setDisable(true);

            System.out.println(
                    "An error occurred while loading cashier orders."
            );

            showMessage(
                    "Unable to load customer orders."
            );
        });

        Thread orderThread =
                new Thread(orderTask);

        orderThread.setDaemon(true);

        orderThread.start();
    }

    @FXML
    private void handleConfirmOrder() {

        AdminOrder selectedOrder =
                orderTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedOrder == null) {

            showMessage(
                    "Please select an order first."
            );

            return;
        }

        if (!"Pending".equals(selectedOrder.getStatus())) {

            showMessage(
                    "Only Pending orders can be confirmed."
            );

            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Confirm Order");
        confirmation.setHeaderText(
                "Confirm Customer Order"
        );

        confirmation.setContentText(
                "Are you sure you want to confirm Order #"
                        + selectedOrder.getId()
                        + "?"
        );

        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response ==
                            javafx.scene.control.ButtonType.OK) {

                        confirmOrder(selectedOrder);
                    }
                });
    }

    private void confirmOrder(AdminOrder selectedOrder) {

        confirmButton.setDisable(true);

        int orderId = selectedOrder.getId();

        Task<Boolean> statusTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return orderDAO.confirmOrder(orderId);
                    }
                };

        statusTask.setOnSucceeded(e -> {

            if (statusTask.getValue()) {

                showMessage(
                        "Order #" + orderId
                                + " confirmed successfully."
                );

                loadOrders();

            } else {

                confirmButton.setDisable(false);

                showMessage(
                        "Unable to confirm the order."
                );
            }
        });

        statusTask.setOnFailed(e -> {

            confirmButton.setDisable(false);

            showMessage(
                    "An error occurred while confirming the order."
            );
        });

        Thread statusThread =
                new Thread(statusTask);

        statusThread.setDaemon(true);

        statusThread.start();
    }

    @FXML
    private void handleRefresh() {

        loadOrders();
    }

    @FXML
    private void handleLogout(ActionEvent event) {

        SessionManager.clearSession();

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/example/projectmvc/view/login-view.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(
                    "CoffeeFlow - Login"
            );

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void showMessage(String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("CoffeeFlow");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}