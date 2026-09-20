package com.example.projectmvc.controller;

import com.example.projectmvc.database.CoffeeDAO;
import com.example.projectmvc.model.CoffeeMenuRow;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import com.example.projectmvc.controller.EditCoffeeController;

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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.concurrent.Task;
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
    private ObservableList<CoffeeMenuRow> coffeeList;
    private FilteredList<CoffeeMenuRow> filteredData;


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

        Task<ObservableList<CoffeeMenuRow>> coffeeTask =
                new Task<>() {

                    @Override
                    protected ObservableList<CoffeeMenuRow> call() {

                        return coffeeDAO
                                .getAllCoffeeMenuRows();
                    }
                };

        coffeeTask.setOnSucceeded(e -> {

            coffeeList =
                    coffeeTask.getValue();

            filteredData =
                    new FilteredList<>(
                            coffeeList,
                            p -> true
                    );

            SortedList<CoffeeMenuRow> sortedData =
                    new SortedList<>(filteredData);

            sortedData.comparatorProperty()
                    .bind(
                            coffeeTable.comparatorProperty()
                    );

            coffeeTable.setItems(sortedData);

            setupSearch();
        });

        coffeeTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while loading coffee."
            );
        });

        Thread coffeeThread =
                new Thread(coffeeTask);

        coffeeThread.setDaemon(true);

        coffeeThread.start();
    }

    private void setupSearch() {


        filteredData =
                new FilteredList<>(coffeeList, p -> true);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filteredData.setPredicate(coffee -> {

                        if (newValue == null ||
                                newValue.isEmpty()) {

                            return true;
                        }

                        String searchText =
                                newValue.toLowerCase();

                        return coffee.getName()
                                .toLowerCase()
                                .contains(searchText);
                    });
                }
        );

        SortedList<CoffeeMenuRow> sortedData =
                new SortedList<>(filteredData);

        sortedData.comparatorProperty()
                .bind(coffeeTable.comparatorProperty());

        coffeeTable.setItems(sortedData);
    }

    @FXML
    private void openEditCoffee(ActionEvent event) throws Exception {

        CoffeeMenuRow selectedCoffee =
                coffeeTable.getSelectionModel().getSelectedItem();

        if (selectedCoffee == null) {
            System.out.println("Please select a coffee first.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/example/projectmvc/view/edit-coffee-view.fxml"
                )
        );

        Parent editPage = loader.load();

        EditCoffeeController controller =
                loader.getController();

        controller.setSelectedCoffee(selectedCoffee);

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(editPage));
        stage.setTitle("CoffeeFlow - Edit Coffee");
        stage.show();
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


    @FXML
    private void handleDelete(ActionEvent event) {

        CoffeeMenuRow selectedCoffee =
                coffeeTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedCoffee == null) {

            System.out.println(
                    "Please select a coffee first."
            );

            return;
        }

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setTitle("Delete Coffee");
        alert.setHeaderText("Delete Coffee");

        alert.setContentText(
                "Are you sure you want to delete \""
                        + selectedCoffee.getName()
                        + "\"?"
        );

        ButtonType result =
                alert.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {
            return;
        }

        String coffeeName =
                selectedCoffee.getName();

        // Background delete and reload task
        Task<ObservableList<CoffeeMenuRow>> deleteTask =
                new Task<>() {

                    @Override
                    protected ObservableList<CoffeeMenuRow> call() {

                        boolean deleted =
                                coffeeDAO.deleteCoffee(
                                        coffeeName
                                );

                        if (!deleted) {
                            return null;
                        }

                        return coffeeDAO
                                .getAllCoffeeMenuRows();
                    }
                };

        // Task completed successfully
        deleteTask.setOnSucceeded(e -> {

            ObservableList<CoffeeMenuRow> updatedList =
                    deleteTask.getValue();

            if (updatedList != null) {

                coffeeList.clear();

                coffeeList.addAll(
                        updatedList
                );

                coffeeTable.refresh();

                System.out.println(
                        "Coffee deleted successfully!"
                );

            } else {

                System.out.println(
                        "Failed to delete coffee."
                );
            }
        });

        // Task failed unexpectedly
        deleteTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while deleting coffee."
            );
        });

        // Start background thread
        Thread deleteThread =
                new Thread(deleteTask);

        deleteThread.setDaemon(true);

        deleteThread.start();
    }
}
