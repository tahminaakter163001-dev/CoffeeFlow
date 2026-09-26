package com.example.projectmvc.controller;

import com.example.projectmvc.SessionManager;
import com.example.projectmvc.database.OrderDAO;
import com.example.projectmvc.model.CustomerOrder;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MyOrdersController {

    @FXML
    private TableView<CustomerOrder> ordersTable;

    @FXML
    private TableColumn<CustomerOrder, Number> orderIdColumn;

    @FXML
    private TableColumn<CustomerOrder, Number> billIdColumn;

    @FXML
    private TableColumn<CustomerOrder, String> coffeeColumn;

    @FXML
    private TableColumn<CustomerOrder, String> sizeColumn;

    @FXML
    private TableColumn<CustomerOrder, Number> quantityColumn;

    @FXML
    private TableColumn<CustomerOrder, Number> priceColumn;

    @FXML
    private TableColumn<CustomerOrder, Number> totalColumn;

    @FXML
    private TableColumn<CustomerOrder, String> orderDateColumn;

    @FXML
    private TableColumn<CustomerOrder, String> orderStatusColumn;

    @FXML
    private TableColumn<CustomerOrder, String> paymentMethodColumn;

    @FXML
    private TableColumn<CustomerOrder, String> paymentStatusColumn;

    private final OrderDAO orderDAO =
            new OrderDAO();

    @FXML
    private void initialize() {

        orderIdColumn.setCellValueFactory(
                data -> data.getValue().orderIdProperty()
        );

        billIdColumn.setCellValueFactory(
                data -> data.getValue().billIdProperty()
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

        orderDateColumn.setCellValueFactory(
                data -> data.getValue().orderDateProperty()
        );

        orderStatusColumn.setCellValueFactory(
                data -> data.getValue().orderStatusProperty()
        );

        paymentMethodColumn.setCellValueFactory(
                data -> data.getValue().paymentMethodProperty()
        );

        paymentStatusColumn.setCellValueFactory(
                data -> data.getValue().paymentStatusProperty()
        );

        // Order status colors
        orderStatusColumn.setCellFactory(column -> {

            return new TableCell<CustomerOrder, String>() {

                @Override
                protected void updateItem(
                        String status,
                        boolean empty) {

                    super.updateItem(
                            status,
                            empty
                    );

                    if (empty || status == null) {

                        setText(null);
                        setStyle("");

                    } else {

                        setText(status);

                        if (status.equals("Ready")) {

                            setStyle(
                                    "-fx-text-fill: green;" +
                                            "-fx-font-weight: bold;"
                            );

                        } else if (
                                status.equals("Cancelled")) {

                            setStyle(
                                    "-fx-text-fill: red;" +
                                            "-fx-font-weight: bold;"
                            );

                        } else {

                            setStyle(
                                    "-fx-text-fill: #6F4E37;" +
                                            "-fx-font-weight: bold;"
                            );
                        }
                    }
                }
            };
        });

        // Payment status colors
        paymentStatusColumn.setCellFactory(column -> {

            return new TableCell<CustomerOrder, String>() {

                @Override
                protected void updateItem(
                        String status,
                        boolean empty) {

                    super.updateItem(
                            status,
                            empty
                    );

                    if (empty || status == null) {

                        setText(null);
                        setStyle("");

                    } else {

                        setText(status);

                        if (status.equals("Paid")) {

                            setStyle(
                                    "-fx-text-fill: green;" +
                                            "-fx-font-weight: bold;"
                            );

                        } else {

                            setStyle(
                                    "-fx-text-fill: orange;" +
                                            "-fx-font-weight: bold;"
                            );
                        }
                    }
                }
            };
        });

        loadOrders();
    }

    private void loadOrders() {

        String customerName =
                SessionManager.getUsername();

        Task<ObservableList<CustomerOrder>> orderTask =
                new Task<>() {

                    @Override
                    protected ObservableList<CustomerOrder> call() {

                        return orderDAO
                                .getCustomerOrdersWithBilling(
                                        customerName
                                );
                    }
                };

        orderTask.setOnSucceeded(e -> {

            ordersTable.setItems(
                    orderTask.getValue()
            );
        });

        orderTask.setOnFailed(e -> {

            System.out.println(
                    "Error while loading customer orders."
            );
        });

        Thread orderThread =
                new Thread(orderTask);

        orderThread.setDaemon(true);

        orderThread.start();
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
                "CoffeeFlow - Customer Dashboard"
        );

        stage.show();
    }
    private void showMessage(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle("CoffeeFlow");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
    @FXML
    private void handleCancelOrder(ActionEvent event) {

        CustomerOrder selectedOrder =
                ordersTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedOrder == null) {

            showMessage(
                    "Please select an order to cancel."
            );

            return;
        }

        if (!selectedOrder
                .getOrderStatus()
                .equals("Pending")) {

            showMessage(
                    "Only Pending orders can be cancelled."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Cancel Order"
        );

        confirmation.setHeaderText(
                "Cancel Bill #" +
                        selectedOrder.getBillId()
        );

        confirmation.setContentText(
                "This will cancel all items in this bill.\n\n"
                        + "Do you want to continue?"
        );

        ButtonType result =
                confirmation.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {
            return;
        }

        String customerName =
                SessionManager.getUsername();

        int billId =
                selectedOrder.getBillId();

        Task<Boolean> cancelTask =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return orderDAO.cancelCustomerBill(
                                billId,
                                customerName
                        );
                    }
                };

        cancelTask.setOnSucceeded(e -> {

            if (cancelTask.getValue()) {

                showMessage(
                        "Bill #" +
                                billId +
                                " and all its orders "
                                + "have been cancelled."
                );

                loadOrders();

            } else {

                showMessage(
                        "This bill could not be cancelled.\n\n"
                                + "Make sure all orders in the bill "
                                + "are still Pending."
                );
            }
        });

        cancelTask.setOnFailed(e -> {

            showMessage(
                    "An error occurred while cancelling "
                            + "the bill."
            );
        });

        Thread cancelThread =
                new Thread(cancelTask);

        cancelThread.setDaemon(true);

        cancelThread.start();
    }
}