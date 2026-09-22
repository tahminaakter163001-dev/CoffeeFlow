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
                    order_date TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Pending',
                    bill_id INTEGER
                );
                """;

        String billsTable = """
                CREATE TABLE IF NOT EXISTS bills (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_name TEXT NOT NULL,
                    total_amount REAL NOT NULL,
                    payment_method TEXT NOT NULL,
                    payment_status TEXT NOT NULL DEFAULT 'Paid',
                    payment_date TEXT NOT NULL
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

            // Add status column to old database
            try {

                statement.execute(
                        "ALTER TABLE orders ADD COLUMN status " +
                                "TEXT NOT NULL DEFAULT 'Pending'"
                );

                System.out.println(
                        "Order status column added successfully!"
                );

            } catch (SQLException e) {

                // Column already exists
            }

            // Add bill_id column to old database
            try {

                statement.execute(
                        "ALTER TABLE orders ADD COLUMN bill_id " +
                                "INTEGER"
                );

                System.out.println(
                        "Bill ID column added successfully!"
                );

            } catch (SQLException e) {

                // Column already exists
            }

            statement.execute(billsTable);

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