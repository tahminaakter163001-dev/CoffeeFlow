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

        loadOrders();
    }

    private void loadOrders() {

        ObservableList<OrderHistory> orders =
                orderDAO.getCustomerOrders(
                        SessionManager.getUsername()
                );

        ordersTable.setItems(orders);
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


