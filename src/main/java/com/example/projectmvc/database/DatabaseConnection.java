package com.example.projectmvc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlite:coffeeflow.db";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(URL);
    }
}
