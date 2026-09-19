package com.example.projectmvc.database;

import com.example.projectmvc.model.Coffee;
import com.example.projectmvc.model.CoffeeMenuRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CoffeeDAO {

    // Add coffee to database
    public void addCoffee(Coffee coffee) {

        String sql = """
                INSERT INTO coffee (name, size, price)
                VALUES (?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, coffee.getName());
            statement.setString(2, coffee.getSize());
            statement.setDouble(3, coffee.getPrice());

            statement.executeUpdate();

            System.out.println(
                    "Coffee added successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Insert error: " + e.getMessage()
            );
        }
    }

    // Retrieve coffees for Menu TableView
    public ObservableList<CoffeeMenuRow> getAllCoffeeMenuRows() {

        ObservableList<CoffeeMenuRow> coffeeList =
                FXCollections.observableArrayList();

        String sql = """
        SELECT
               ROW_NUMBER() OVER (ORDER BY MIN(id)) AS menu_id,
               name,
               MAX(CASE WHEN size = 'Small' THEN price ELSE 0 END) AS small_price,
               MAX(CASE WHEN size = 'Medium' THEN price ELSE 0 END) AS medium_price,
               MAX(CASE WHEN size = 'Large' THEN price ELSE 0 END) AS large_price
        FROM coffee
        GROUP BY name
        ORDER BY MIN(id)
        """;


        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int id =
                        resultSet.getInt("menu_id");

                String name =
                        resultSet.getString("name");

                double smallPrice =
                        resultSet.getDouble("small_price");

                double mediumPrice =
                        resultSet.getDouble("medium_price");

                double largePrice =
                        resultSet.getDouble("large_price");

                CoffeeMenuRow coffee =
                        new CoffeeMenuRow(
                                id,
                                name,
                                smallPrice,
                                mediumPrice,
                                largePrice
                        );

                coffeeList.add(coffee);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Menu retrieve error: " + e.getMessage()
            );
        }

        return coffeeList;
    }

    public void updateCoffee(
            String oldName,
            String newName,
            double smallPrice,
            double mediumPrice,
            double largePrice) {

        String sql = """
            UPDATE coffee
            SET name = ?,
                price = CASE
                    WHEN size = 'Small' THEN ?
                    WHEN size = 'Medium' THEN ?
                    WHEN size = 'Large' THEN ?
                END
            WHERE name = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newName);
            statement.setDouble(2, smallPrice);
            statement.setDouble(3, mediumPrice);
            statement.setDouble(4, largePrice);
            statement.setString(5, oldName);

            statement.executeUpdate();

            System.out.println(
                    "Coffee updated successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Update error: " + e.getMessage()
            );
        }
    }
}
