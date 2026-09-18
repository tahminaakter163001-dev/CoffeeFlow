package com.example.projectmvc.controller;

import com.example.projectmvc.database.CoffeeDAO;
import com.example.projectmvc.model.CoffeeMenuRow;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<CoffeeMenuRow> coffeeTable;

    @FXML
    private TableColumn<CoffeeMenuRow, Number> idColumn;

    @FXML
    private TableColumn<CoffeeMenuRow, String> nameColumn;

    @FXML
    private TableColumn<CoffeeMenuRow, Number> smallPriceColumn;

    @FXML
    private TableColumn<CoffeeMenuRow, Number> mediumPriceColumn;

    @FXML
    private TableColumn<CoffeeMenuRow, Number> largePriceColumn;

    private final CoffeeDAO coffeeDAO = new CoffeeDAO();

    @FXML
    private void initialize() {

        idColumn.setCellValueFactory(
                cellData -> cellData.getValue().idProperty()
        );

        nameColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty()
        );

        smallPriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().smallPriceProperty()
        );

        mediumPriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().mediumPriceProperty()
        );

        largePriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().largePriceProperty()
        );

        loadCoffeeData();
    }

    private void loadCoffeeData() {

        ObservableList<CoffeeMenuRow> coffeeList =
                coffeeDAO.getAllCoffeeMenuRows();

        coffeeTable.setItems(coffeeList);
    }

    @FXML
    private void openAddCoffee(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/add-coffee-view.fxml"
                )
        );

        Parent addCoffeePage = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(addCoffeePage));
        stage.setTitle("CoffeeFlow - Add Coffee");
        stage.show();
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/dashboard-view.fxml"
                )
        );

        Parent dashboard = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(dashboard));
        stage.setTitle("CoffeeFlow - Dashboard");
        stage.show();
    }
}
