package com.example.projectmvc.controller;

import com.example.projectmvc.database.OrderDAO;
import com.example.projectmvc.model.AdminOrder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.control.TableCell;

public class AdminOrdersController {

    @FXML
    private TableView<AdminOrder> ordersTable;

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
    private javafx.scene.control.ComboBox<String> statusComboBox;

    @FXML
    private javafx.scene.control.Button updateStatusButton;


    private final OrderDAO orderDAO =
            new OrderDAO();

    @FXML
    public void initialize() {

        statusComboBox.getItems().addAll(
                "Pending",
                "Preparing",
                "Ready",
                "Completed"
        );

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
        statusColumn.setCellFactory(column -> {

            return new TableCell<AdminOrder, String>() {

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
        ordersTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldOrder, newOrder) -> {

                            if (newOrder != null) {

                                statusComboBox.setValue(
                                        newOrder.getStatus()
                                );

                            } else {

                                statusComboBox.setValue(null);
                            }
                        }
                );

        loadOrders();
    }

    private void loadOrders() {

        // Background database task
        Task<ObservableList<AdminOrder>> orderTask =
                new Task<>() {

                    @Override
                    protected ObservableList<AdminOrder> call() {

                        return orderDAO.getAllOrders();
                    }
                };

        // Task completed successfully
        orderTask.setOnSucceeded(e -> {

            ObservableList<AdminOrder> orders =
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
    private void handleUpdateStatus() {

        AdminOrder selectedOrder =
                ordersTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedOrder == null) {

            System.out.println(
                    "Please select an order first."
            );

            return;
        }

        String newStatus =
                statusComboBox.getValue();

        if (newStatus == null) {

            System.out.println(
                    "Please select a status."
            );

            return;
        }

        int orderId =
                selectedOrder.getId();

        updateStatusButton.setDisable(true);

        Task<Boolean> statusTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return orderDAO.updateOrderStatus(
                                orderId,
                                newStatus
                        );
                    }
                };

        statusTask.setOnSucceeded(e -> {

            updateStatusButton.setDisable(false);

            boolean success =
                    statusTask.getValue();

            if (success) {

                selectedOrder
                        .statusProperty()
                        .set(newStatus);

                statusComboBox.setValue(null);

                ordersTable.refresh();

                System.out.println(
                        "Order status updated successfully!"
                );

            } else {

                System.out.println(
                        "Failed to update order status."
                );
            }
        });

        statusTask.setOnFailed(e -> {

            updateStatusButton.setDisable(false);

            System.out.println(
                    "An error occurred while updating order status."
            );
        });

        Thread statusThread =
                new Thread(statusTask);

        statusThread.setDaemon(true);

        statusThread.start();
    }

    @FXML
    private void goBack(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/dashboard-view.fxml"
                        )
                );

        Parent dashboardPage =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(dashboardPage)
        );

        stage.setTitle(
                "CoffeeFlow - Admin Dashboard"
        );

        stage.show();
    }
}

