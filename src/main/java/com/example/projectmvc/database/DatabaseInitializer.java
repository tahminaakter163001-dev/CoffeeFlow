package com.example.projectmvc.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTables() {

        String coffeeTable = """
                CREATE TABLE IF NOT EXISTS coffee (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    size TEXT NOT NULL,
                    price REAL NOT NULL
                );
                """;

        String ordersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_name TEXT NOT NULL,
                    coffee_name TEXT NOT NULL,
                    size TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    price REAL NOT NULL,
                    total REAL NOT NULL,
                    order_date TEXT NOT NULL
                );
                """;
        String usersTable = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            role TEXT NOT NULL
        );
        """;
        String adminUser = """
        INSERT OR IGNORE INTO users
        (username, password, role)
        VALUES ('admin', '1234', 'admin');
        """;

        String customerUser = """
        INSERT OR IGNORE INTO users
        (username, password, role)
        VALUES ('customer', '1234', 'customer');
        """;
        try (Connection connection =
                     DatabaseConnection.getConnection();
             Statement statement =
                     connection.createStatement()) {

            statement.execute(coffeeTable);
            statement.execute(ordersTable);
            statement.execute(usersTable);

            statement.execute(adminUser);
            statement.execute(customerUser);

            System.out.println(
                    "Database tables created successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage()
            );
        }
    }
}
