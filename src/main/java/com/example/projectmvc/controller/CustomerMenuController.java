package com.example.projectmvc.controller;

import com.example.projectmvc.database.CoffeeDAO;
import com.example.projectmvc.model.CoffeeMenuRow;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerMenuController {

    @FXML
    private FlowPane coffeeContainer;

    private final CoffeeDAO coffeeDAO = new CoffeeDAO();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(2, runnable -> {
                Thread thread = new Thread(
                        runnable,
                        "coffeeflow-customer-menu-worker"
                );
                thread.setDaemon(true);
                return thread;
            });

    @FXML
    public void initialize() {

        loadCoffeeMenu();
    }

    private void loadCoffeeMenu() {

        Task<ObservableList<CoffeeMenuRow>> task =
                new Task<>() {

                    @Override
                    protected ObservableList<CoffeeMenuRow> call() {

                        return coffeeDAO.getAllCoffeeMenuRows();
                    }
                };

        task.setOnSucceeded(event -> {

            coffeeContainer.getChildren().clear();

            ObservableList<CoffeeMenuRow> coffeeList =
                    task.getValue();

            for (CoffeeMenuRow coffee : coffeeList) {

                coffeeContainer.getChildren()
                        .add(createCoffeeCard(coffee));
            }
        });

        task.setOnFailed(event -> {

            Label errorLabel =
                    new Label("Unable to load coffee menu.");

            errorLabel.setStyle(
                    "-fx-text-fill: #8B3A3A;" +
                            "-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;"
            );

            coffeeContainer.getChildren().clear();
            coffeeContainer.getChildren().add(errorLabel);

            task.getException().printStackTrace();
        });

        executor.execute(task);
    }

    private VBox createCoffeeCard(CoffeeMenuRow coffee) {

        VBox card = new VBox(10);

        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPrefWidth(260);
        card.setMinHeight(350);
        card.setPadding(new javafx.geometry.Insets(15));

        card.setStyle(
                "-fx-background-color: #FFF9F3;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-color: #D6C2B3;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(60,40,25,0.15), 12, 0, 0, 4);"
        );

        // Coffee Image
        ImageView coffeeImage = new ImageView(
                new Image(
                        getCoffeeImageUrl(coffee.getName()),
                        220,
                        125,
                        true,
                        true,
                        true
                )
        );

        coffeeImage.setFitWidth(220);
        coffeeImage.setFitHeight(125);
        coffeeImage.setPreserveRatio(true);
        coffeeImage.setSmooth(true);

        // Rounded image corners
        javafx.scene.shape.Rectangle clip =
                new javafx.scene.shape.Rectangle(220, 125);

        clip.setArcWidth(20);
        clip.setArcHeight(20);

        coffeeImage.setClip(clip);

        Label name = new Label(coffee.getName());

        name.setWrapText(true);
        name.setStyle(
                "-fx-text-fill: #4A3428;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;"
        );

        Label small = new Label(
                "Small     ৳ " + formatPrice(coffee.getSmallPrice())
        );

        Label medium = new Label(
                "Medium   ৳ " + formatPrice(coffee.getMediumPrice())
        );

        Label large = new Label(
                "Large     ৳ " + formatPrice(coffee.getLargePrice())
        );

        String priceStyle =
                "-fx-text-fill: #7A6658;" +
                        "-fx-font-size: 13px;";

        small.setStyle(priceStyle);
        medium.setStyle(priceStyle);
        large.setStyle(priceStyle);

        Button orderButton = new Button("ORDER NOW  →");

        orderButton.setPrefWidth(180);
        orderButton.setPrefHeight(38);

        orderButton.setStyle(
                "-fx-background-color: #6F4E37;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

        orderButton.setOnAction(event ->
                openLogin(event)
        );

        card.getChildren().addAll(
                coffeeImage,
                name,
                small,
                medium,
                large,
                orderButton
        );

        return card;
    }

    private String formatPrice(double price) {

        if (price == 0) {
            return "N/A";
        }

        return String.format("%.2f", price);
    }

    private void openLogin(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/login-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource())
                    .getScene();

            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();

            stage.setTitle("CoffeeFlow");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {

        executor.shutdownNow();

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/projectmvc/view/home-view.fxml"
                    )
            );

            Parent root = loader.load();

            Scene scene = ((Node) event.getSource())
                    .getScene();

            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();

            stage.setTitle("CoffeeFlow");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    private String getCoffeeImageUrl(String coffeeName) {

        String name = coffeeName.toLowerCase();

        if (name.contains("cappuccino")) {
            return "https://images.unsplash.com/photo-1572442388796-11668a67e53d?auto=format&fit=crop&w=600&q=80";
        }

        if (name.contains("latte")) {
            return "https://images.unsplash.com/photo-1541167760496-1628856ab772?auto=format&fit=crop&w=600&q=80";
        }

        if (name.contains("espresso")) {
            return "https://images.unsplash.com/photo-1530798985-ca4c54a2f42a?auto=format&fit=crop&fm=jpg&q=80&w=600";
        }

        if (name.contains("americano")) {
            return "https://images.unsplash.com/photo-1497515114629-f71d768fd07c?auto=format&fit=crop&w=600&q=80";
        }

        // Default coffee image
        return "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=600&q=80";
    }
}