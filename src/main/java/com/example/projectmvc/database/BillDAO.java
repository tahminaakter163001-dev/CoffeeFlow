package com.example.projectmvc.database;

import com.example.projectmvc.model.Bill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillDAO {

    public ObservableList<Bill> getAllBills() {

        ObservableList<Bill> billList =
                FXCollections.observableArrayList();

        String sql = """
            SELECT id,
                   customer_name,
                   total_amount,
                   payment_method,
                   payment_status,
                   payment_date
            FROM bills
            ORDER BY id DESC
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Bill bill =
                        new Bill(
                                resultSet.getInt("id"),
                                resultSet.getString("customer_name"),
                                resultSet.getDouble("total_amount"),
                                resultSet.getString("payment_method"),
                                resultSet.getString("payment_status"),
                                resultSet.getString("payment_date")
                        );

                billList.add(bill);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Bill retrieve error: "
                            + e.getMessage()
            );
        }

        return billList;
    }
}