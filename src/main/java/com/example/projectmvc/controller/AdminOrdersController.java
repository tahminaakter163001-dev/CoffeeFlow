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

    private final OrderDAO orderDAO =
            new OrderDAO();

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

        loadOrders();
    }

    private void loadOrders() {

        ObservableList<AdminOrder> orders =
                orderDAO.getAllOrders();

        ordersTable.setItems(orders);
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

