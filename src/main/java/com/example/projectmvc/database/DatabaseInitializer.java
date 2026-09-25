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
            statement.execute("PRAGMA foreign_keys = ON");

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
            addBillForeignKey(connection);

            System.out.println(
                    "Database tables created successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage()
            );
        }
    }
    private static void addBillForeignKey(Connection connection)
            throws SQLException {

        String checkSql = """
            PRAGMA foreign_key_list(orders)
            """;

        boolean foreignKeyExists = false;

        try (Statement statement = connection.createStatement();
             var resultSet = statement.executeQuery(checkSql)) {

            while (resultSet.next()) {

                String tableName = resultSet.getString("table");

                String fromColumn = resultSet.getString("from");

                String toColumn = resultSet.getString("to");

                if ("bills".equalsIgnoreCase(tableName)
                        && "bill_id".equalsIgnoreCase(fromColumn)
                        && "id".equalsIgnoreCase(toColumn)) {

                    foreignKeyExists = true;
                    break;
                }
            }
        }

        if (foreignKeyExists) {

            System.out.println(
                    "Bill foreign key already exists."
            );

            return;
        }

        System.out.println(
                "Adding bill foreign key..."
        );

        connection.setAutoCommit(false);

        try (Statement statement = connection.createStatement()) {

            statement.execute("""
                CREATE TABLE orders_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_name TEXT NOT NULL,
                    coffee_name TEXT NOT NULL,
                    size TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    price REAL NOT NULL,
                    total REAL NOT NULL,
                    order_date TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Pending',
                    bill_id INTEGER,
                    FOREIGN KEY (bill_id) REFERENCES bills(id)
                );
                """);

            statement.execute("""
                INSERT INTO orders_new
                (id, customer_name, coffee_name, size, quantity,
                 price, total, order_date, status, bill_id)
                SELECT
                    id, customer_name, coffee_name, size, quantity,
                    price, total, order_date, status, bill_id
                FROM orders;
                """);

            statement.execute("DROP TABLE orders;");

            statement.execute(
                    "ALTER TABLE orders_new RENAME TO orders;"
            );

            connection.commit();

            System.out.println(
                    "Bill foreign key added successfully!"
            );

        } catch (SQLException e) {

            connection.rollback();

            System.out.println(
                    "Foreign key migration error: "
                            + e.getMessage()
            );

            throw e;

        } finally {

            connection.setAutoCommit(true);
        }
    }

}