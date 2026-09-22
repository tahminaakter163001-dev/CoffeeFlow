package com.example.projectmvc.controller;
import com.example.projectmvc.SessionManager;
import com.example.projectmvc.database.OrderDAO;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class DashboardController {

    public void openCoffeeMenu(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/menu-view.fxml"
                )
        );

        Parent menuPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        Scene scene = new Scene(menuPage);

        stage.setScene(scene);
        stage.setTitle("CoffeeFlow - Coffee Menu");
        stage.show();
    }
    private final OrderDAO orderDAO =
            new OrderDAO();

    @FXML
    private Label pendingCount;

    @FXML
    private Label preparingCount;

    @FXML
    private Label readyCount;

    @FXML
    private Label completedCount;

    @FXML
    private Label cancelledCount;


    @FXML
    private void initialize() {

        loadOrderSummary();
    }
    private void loadOrderSummary() {

        Task<int[]> summaryTask =
                new Task<>() {

                    @Override
                    protected int[] call() {

                        int pending =
                                orderDAO.getOrderCountByStatus(
                                        "Pending"
                                );

                        int preparing =
                                orderDAO.getOrderCountByStatus(
                                        "Preparing"
                                );

                        int ready =
                                orderDAO.getOrderCountByStatus(
                                        "Ready"
                                );

                        int completed =
                                orderDAO.getOrderCountByStatus(
                                        "Completed"
                                );

                        int cancelled =
                                orderDAO.getOrderCountByStatus(
                                        "Cancelled"
                                );

                        return new int[]{
                                pending,
                                preparing,
                                ready,
                                completed,
                                cancelled
                        };
                    }
                };

        summaryTask.setOnSucceeded(e -> {

            int[] counts =
                    summaryTask.getValue();

            pendingCount.setText(
                    String.valueOf(counts[0])
            );

            preparingCount.setText(
                    String.valueOf(counts[1])
            );

            readyCount.setText(
                    String.valueOf(counts[2])
            );

            completedCount.setText(
                    String.valueOf(counts[3])
            );

            cancelledCount.setText(
                    String.valueOf(counts[4])
            );
        });

        summaryTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while loading order summary."
            );
        });

        Thread summaryThread =
                new Thread(summaryTask);

        summaryThread.setDaemon(true);

        summaryThread.start();
    }

    @FXML
    private void openOrder(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/order-view.fxml"
                )
        );

        Parent orderPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(orderPage));
        stage.setTitle("CoffeeFlow - New Order");
        stage.show();
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {

        SessionManager.clearSession();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/login-view.fxml"
                )
        );

        Parent loginPage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(loginPage));
        stage.setTitle("CoffeeFlow - Login");
        stage.show();
    }
    @FXML
    private void openOrders(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/admin-orders-view.fxml"
                        )
                );

        Parent ordersPage =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(ordersPage)
        );

        stage.setTitle(
                "CoffeeFlow - Orders"
        );

        stage.show();
    }
    @FXML
    private void openBilling(ActionEvent event)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/example/projectmvc/view/admin-billing-view.fxml"
                        )
                );

        Parent billingPage =
                loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(billingPage)
        );

        stage.setTitle(
                "CoffeeFlow - Billing"
        );

        stage.show();
    }
}
