package com.example.projectmvc.controller;

import com.example.projectmvc.database.OrderDAO;
import com.example.projectmvc.model.OrderHistory;
import com.example.projectmvc.SessionManager;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;

public class MyOrdersController {

    @FXML
    private TableView<OrderHistory> ordersTable;

    @FXML
    private TableColumn<OrderHistory, Number> idColumn;

    @FXML
    private TableColumn<OrderHistory, String> coffeeColumn;

    @FXML
    private TableColumn<OrderHistory, String> sizeColumn;

    @FXML
    private TableColumn<OrderHistory, Number> quantityColumn;

    @FXML
    private TableColumn<OrderHistory, Number> priceColumn;

    @FXML
    private TableColumn<OrderHistory, Number> totalColumn;

    @FXML
    private TableColumn<OrderHistory, String> dateColumn;

    @FXML
    private TableColumn<OrderHistory, String> statusColumn;

    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    private void initialize() {

        idColumn.setCellValueFactory(
                data -> data.getValue().idProperty()
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
        statusColumn.setCellFactory(column -> {

            return new TableCell<OrderHistory, String>() {

                @Override
                protected void updateItem(
                        String status,
                        boolean empty) {

                    super.updateItem(status, empty);

                    if (empty || status == null) {

                        setText(null);
                        setStyle("");

                    } else {

                        setText(status);

                        switch (status) {

                            case "Cancelled":
                                setStyle(
                                        "-fx-text-fill: red;" +
                                                "-fx-font-weight: bold;"
                                );
                                break;

                            case "Completed":
                                setStyle(
                                        "-fx-text-fill: green;" +
                                                "-fx-font-weight: bold;"
                                );
                                break;

                            case "Pending":
                                setStyle(
                                        "-fx-text-fill: orange;" +
                                                "-fx-font-weight: bold;"
                                );
                                break;

                            case "Preparing":
                                setStyle(
                                        "-fx-text-fill: blue;" +
                                                "-fx-font-weight: bold;"
                                );
                                break;

                            case "Ready":
                                setStyle(
                                        "-fx-text-fill: purple;" +
                                                "-fx-font-weight: bold;"
                                );
                                break;

                            default:
                                setStyle("");
                        }
                    }
                }
            };
        });

        loadOrders();
    }

    private void loadOrders() {

        String username =
                SessionManager.getUsername();

        // Background database task
        Task<ObservableList<OrderHistory>> orderTask =
                new Task<>() {

                    @Override
                    protected ObservableList<OrderHistory> call() {

                        return orderDAO.getCustomerOrders(
                                username
                        );
                    }
                };

        // Task completed successfully
        orderTask.setOnSucceeded(e -> {

            ObservableList<OrderHistory> orders =
                    orderTask.getValue();

            ordersTable.setItems(orders);
        });

        // Task failed unexpectedly
        orderTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while loading orders."
            );
        });

        // Start background thread
        Thread orderThread =
                new Thread(orderTask);

        orderThread.setDaemon(true);

        orderThread.start();
    }

    @FXML
    private void handleCancelOrder() {

        OrderHistory selectedOrder =
                ordersTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedOrder == null) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Cancel Order");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Please select an order first."
            );

            alert.showAndWait();

            return;
        }

        String currentStatus =
                selectedOrder.getStatus();

        if (!currentStatus.equals("Pending")) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Cancel Order");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Only Pending orders can be cancelled."
            );

            alert.showAndWait();

            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Cancel Order");
        confirmation.setHeaderText("Cancel Order");

        confirmation.setContentText(
                "Are you sure you want to cancel Order ID "
                        + selectedOrder.getId()
                        + "?"
        );

        ButtonType result =
                confirmation.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {
            return;
        }

        int orderId =
                selectedOrder.getId();

        Task<Boolean> cancelTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return orderDAO.updateOrderStatus(
                                orderId,
                                "Cancelled"
                        );
                    }
                };

        cancelTask.setOnSucceeded(e -> {

            boolean success =
                    cancelTask.getValue();

            if (success) {

                selectedOrder
                        .statusProperty()
                        .set("Cancelled");

                ordersTable.refresh();

                Alert alert =
                        new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Order Cancelled");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Order cancelled successfully."
                );

                alert.showAndWait();

            } else {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Cancel Order");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Failed to cancel the order."
                );

                alert.showAndWait();
            }
        });

        cancelTask.setOnFailed(e -> {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Cancel Order");
            alert.setHeaderText(null);
            alert.setContentText(
                    "An error occurred while cancelling the order."
            );

            alert.showAndWait();
        });

        Thread cancelThread =
                new Thread(cancelTask);

        cancelThread.setDaemon(true);

        cancelThread.start();
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


