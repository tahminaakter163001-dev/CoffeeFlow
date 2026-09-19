package com.example.projectmvc.database;

import com.example.projectmvc.model.OrderItem;
import com.example.projectmvc.model.OrderHistory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderDAO {

    public boolean saveOrder(
            String customerName,
            OrderItem item) {

        String sql = """
            INSERT INTO orders
            (customer_name, coffee_name, size,
             quantity, price, total, order_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, customerName
            );

            statement.setString(
                    2, item.getCoffeeName()
            );

            statement.setString(
                    3, item.getSize()
            );

            statement.setInt(
                    4, item.getQuantity()
            );

            statement.setDouble(
                    5, item.getPrice()
            );

            statement.setDouble(
                    6, item.getTotal()
            );

            String orderDate =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd HH:mm:ss"
                                    )
                            );

            statement.setString(
                    7, orderDate
            );

            statement.executeUpdate();

            System.out.println(
                    "Order saved successfully!"
            );

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Order save error: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public ObservableList<OrderHistory> getCustomerOrders(
            String customerName) {

        ObservableList<OrderHistory> orderList =
                FXCollections.observableArrayList();

        String sql = """
            SELECT id, coffee_name, size,
                   quantity, price, total, order_date
            FROM orders
            WHERE customer_name = ?
            ORDER BY id DESC
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, customerName
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    OrderHistory order =
                            new OrderHistory(
                                    resultSet.getInt("id"),
                                    resultSet.getString("coffee_name"),
                                    resultSet.getString("size"),
                                    resultSet.getInt("quantity"),
                                    resultSet.getDouble("price"),
                                    resultSet.getDouble("total"),
                                    resultSet.getString("order_date")
                            );

                    orderList.add(order);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Order history error: "
                            + e.getMessage()
            );
        }

        return orderList;
    }
}
