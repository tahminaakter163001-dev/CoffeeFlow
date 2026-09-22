package com.example.projectmvc.controller;

import com.example.projectmvc.database.BillDAO;
import com.example.projectmvc.model.Bill;

import javafx.collections.FXCollections;
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
import javafx.stage.Stage;

public class AdminBillingController {

    @FXML
    private TableView<Bill> billingTable;

    @FXML
    private TableColumn<Bill, Number> idColumn;

    @FXML
    private TableColumn<Bill, String> customerColumn;

    @FXML
    private TableColumn<Bill, Number> amountColumn;

    @FXML
    private TableColumn<Bill, String> methodColumn;

    @FXML
    private TableColumn<Bill, String> statusColumn;

    @FXML
    private TableColumn<Bill, String> dateColumn;

    private final BillDAO billDAO =
            new BillDAO();


    @FXML
    private void initialize() {

        idColumn.setCellValueFactory(
                data -> data.getValue().idProperty()
        );

        customerColumn.setCellValueFactory(
                data -> data.getValue().customerNameProperty()
        );

        amountColumn.setCellValueFactory(
                data -> data.getValue().totalAmountProperty()
        );

        methodColumn.setCellValueFactory(
                data -> data.getValue().paymentMethodProperty()
        );

        statusColumn.setCellValueFactory(
                data -> data.getValue().paymentStatusProperty()
        );

        dateColumn.setCellValueFactory(
                data -> data.getValue().paymentDateProperty()
        );


        statusColumn.setCellFactory(column -> {

            return new TableCell<Bill, String>() {

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


        loadBills();
    }


    private void loadBills() {

        Task<ObservableList<Bill>> billTask =
                new Task<>() {

                    @Override
                    protected ObservableList<Bill> call() {

                        return billDAO.getAllBills();
                    }
                };


        billTask.setOnSucceeded(e -> {

            billingTable.setItems(
                    billTask.getValue()
            );
        });


        billTask.setOnFailed(e -> {

            System.out.println(
                    "An error occurred while loading bills."
            );
        });


        Thread billThread =
                new Thread(billTask);

        billThread.setDaemon(true);

        billThread.start();
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