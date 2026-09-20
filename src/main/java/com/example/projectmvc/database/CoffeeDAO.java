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
    public boolean addCoffee(Coffee coffee) {

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

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Insert error: " + e.getMessage()
            );

            return false;
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

    public boolean updateCoffee(
            String oldName,
            String newName,
            double smallPrice,
            double mediumPrice,
            double largePrice) {

        String deleteSql = """
            DELETE FROM coffee
            WHERE name = ?
            """;

        String insertSql = """
            INSERT INTO coffee (name, size, price)
            VALUES (?, ?, ?)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (
                    PreparedStatement deleteStatement =
                            connection.prepareStatement(deleteSql);

                    PreparedStatement insertStatement =
                            connection.prepareStatement(insertSql)
            ) {

                // Delete old coffee
                deleteStatement.setString(1, oldName);
                deleteStatement.executeUpdate();

                // Insert Small
                insertStatement.setString(1, newName);
                insertStatement.setString(2, "Small");
                insertStatement.setDouble(3, smallPrice);
                insertStatement.executeUpdate();

                // Insert Medium
                insertStatement.setString(1, newName);
                insertStatement.setString(2, "Medium");
                insertStatement.setDouble(3, mediumPrice);
                insertStatement.executeUpdate();

                // Insert Large
                insertStatement.setString(1, newName);
                insertStatement.setString(2, "Large");
                insertStatement.setDouble(3, largePrice);
                insertStatement.executeUpdate();

                connection.commit();

                System.out.println(
                        "Coffee updated successfully!"
                );

                return true;

            } catch (SQLException e) {

                connection.rollback();

                System.out.println(
                        "Update error: " + e.getMessage()
                );

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage()
            );

            return false;
        }
    }

    public boolean deleteCoffee(String name) {

        String sql = """
            DELETE FROM coffee
            WHERE name = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);

            statement.executeUpdate();

            System.out.println(
                    "Coffee deleted successfully!"
            );

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Delete error: " + e.getMessage()
            );

            return false;
        }
    }

}
