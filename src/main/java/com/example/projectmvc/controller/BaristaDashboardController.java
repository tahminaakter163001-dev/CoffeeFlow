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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class BaristaDashboardController {

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
    private Button preparingButton;

    @FXML
    private Button readyButton;

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

        preparingButton.setDisable(true);
        readyButton.setDisable(true);

        orderTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldOrder, newOrder) -> {

                            updateButtonState(newOrder);
                        }
                );

        loadOrders();
    }

    private void updateButtonState(AdminOrder order) {

        preparingButton.setDisable(true);
        readyButton.setDisable(true);

        if (order == null) {
            return;
        }

        if ("Confirmed".equals(order.getStatus())) {

            preparingButton.setDisable(false);

        } else if ("Preparing".equals(order.getStatus())) {

            readyButton.setDisable(false);
        }
    }

    private void loadOrders() {

        preparingButton.setDisable(true);
        readyButton.setDisable(true);

        Task<ObservableList<AdminOrder>> orderTask =
                new Task<>() {

                    @Override
                    protected ObservableList<AdminOrder> call() {

                        return orderDAO.getBaristaOrders();
                    }
                };

        orderTask.setOnSucceeded(e -> {

            ObservableList<AdminOrder> orders =
                    orderTask.getValue();

            orderTable.setItems(orders);
        });

        orderTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while loading barista orders."
            );

            showMessage(
                    "Unable to load barista orders."
            );
        });

        Thread orderThread =
                new Thread(orderTask);

        orderThread.setDaemon(true);

        orderThread.start();
    }

    @FXML
    private void handleStartPreparing() {

        AdminOrder selectedOrder =
                orderTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedOrder == null) {

            showMessage(
                    "Please select an order first."
            );

            return;
        }

        if (!"Confirmed".equals(selectedOrder.getStatus())) {

            showMessage(
                    "Only Confirmed orders can be started."
            );

            return;
        }

        updateStatus(
                selectedOrder,
                "Preparing"
        );
    }

    @FXML
    private void handleMarkReady() {

        AdminOrder selectedOrder =
                orderTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedOrder == null) {

            showMessage(
                    "Please select an order first."
            );

            return;
        }

        if (!"Preparing".equals(selectedOrder.getStatus())) {

            showMessage(
                    "Only Preparing orders can be marked Ready."
            );

            return;
        }

        updateStatus(
                selectedOrder,
                "Ready"
        );
    }

    private void updateStatus(
            AdminOrder selectedOrder,
            String newStatus) {

        preparingButton.setDisable(true);
        readyButton.setDisable(true);

        int orderId =
                selectedOrder.getId();

        Task<Boolean> statusTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return orderDAO.updateBaristaOrderStatus(
                                orderId,
                                newStatus
                        );
                    }
                };

        statusTask.setOnSucceeded(e -> {

            if (statusTask.getValue()) {

                selectedOrder
                        .statusProperty()
                        .set(newStatus);

                orderTable.refresh();

                showMessage(
                        "Order #" + orderId
                                + " is now "
                                + newStatus + "."
                );

                loadOrders();

            } else {

                updateButtonState(selectedOrder);

                showMessage(
                        "Unable to update order status."
                );
            }
        });

        statusTask.setOnFailed(e -> {

            updateButtonState(selectedOrder);

            showMessage(
                    "An error occurred while updating order status."
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