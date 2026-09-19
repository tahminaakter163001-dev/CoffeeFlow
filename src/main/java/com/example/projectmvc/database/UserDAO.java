package com.example.projectmvc.database;

import com.example.projectmvc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(User user) {

        String sql = """
                INSERT INTO users
                (username, password, role)
                VALUES (?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Registration error: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public boolean usernameExists(String username) {

        String sql = """
                SELECT id
                FROM users
                WHERE username = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Username check error: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public User loginUser(
            String username,
            String password) {

        String sql = """
                SELECT id, username, password, role
                FROM users
                WHERE username = ?
                AND password = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Login error: "
                            + e.getMessage()
            );
        }

        return null;
    }
}

