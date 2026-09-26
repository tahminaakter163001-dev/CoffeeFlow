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
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {

    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    private Label totalOrdersCount;

    @FXML
    private Label pendingCount;

    @FXML
    private Label confirmedCount;

    @FXML
    private Label preparingCount;

    @FXML
    private Label readyCount;

    @FXML
    private Label cancelledCount;

    @FXML
    private Label totalSalesCount;


    @FXML
    private void initialize() {
        loadOrderSummary();
    }


    private void loadOrderSummary() {

        Task<Object[]> summaryTask = new Task<>() {

            @Override
            protected Object[] call() {

                int totalOrders =
                        orderDAO.getOrderCountByStatus("Pending")
                                + orderDAO.getOrderCountByStatus("Confirmed")
                                + orderDAO.getOrderCountByStatus("Preparing")
                                + orderDAO.getOrderCountByStatus("Ready")
                                + orderDAO.getOrderCountByStatus("Cancelled");

                int pending =
                        orderDAO.getOrderCountByStatus("Pending");

                int confirmed =
                        orderDAO.getOrderCountByStatus("Confirmed");

                int preparing =
                        orderDAO.getOrderCountByStatus("Preparing");

                int ready =
                        orderDAO.getOrderCountByStatus("Ready");

                int cancelled =
                        orderDAO.getOrderCountByStatus("Cancelled");

                double totalSales =
                        orderDAO.getTotalSales();

                return new Object[]{
                        totalOrders,
                        pending,
                        confirmed,
                        preparing,
                        ready,
                        cancelled,
                        totalSales
                };
            }
        };


        summaryTask.setOnSucceeded(e -> {

            Object[] summary = summaryTask.getValue();

            totalOrdersCount.setText(
                    String.valueOf(summary[0])
            );

            pendingCount.setText(
                    String.valueOf(summary[1])
            );

            confirmedCount.setText(
                    String.valueOf(summary[2])
            );

            preparingCount.setText(
                    String.valueOf(summary[3])
            );

            readyCount.setText(
                    String.valueOf(summary[4])
            );

            cancelledCount.setText(
                    String.valueOf(summary[5])
            );

            totalSalesCount.setText(
                    String.format("%.2f Tk", (Double) summary[6])
            );
        });


        summaryTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while loading admin summary."
            );
        });


        Thread summaryThread =
                new Thread(summaryTask);

        summaryThread.setDaemon(true);

        summaryThread.start();
    }


    @FXML
    private void openCoffeeMenu(ActionEvent event) throws Exception {

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
}