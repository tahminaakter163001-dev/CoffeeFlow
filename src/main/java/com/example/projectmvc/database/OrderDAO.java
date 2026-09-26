package com.example.projectmvc.database;

import com.example.projectmvc.model.OrderItem;
import com.example.projectmvc.model.OrderHistory;
import com.example.projectmvc.model.AdminOrder;
import com.example.projectmvc.model.CustomerOrder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderDAO {

    public ObservableList<AdminOrder> getCashierOrders() {

        ObservableList<AdminOrder> orders =
                FXCollections.observableArrayList();

        String sql = """
        SELECT id,
               customer_name,
               coffee_name,
               size,
               quantity,
               price,
               total,
               order_date,
               status
        FROM orders
        WHERE status = 'Pending'
           OR status = 'Confirmed'
        ORDER BY id ASC
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AdminOrder order =
                        new AdminOrder(
                                resultSet.getInt("id"),
                                resultSet.getString("customer_name"),
                                resultSet.getString("coffee_name"),
                                resultSet.getString("size"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("price"),
                                resultSet.getDouble("total"),
                                resultSet.getString("order_date"),
                                resultSet.getString("status")
                        );

                orders.add(order);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Cashier order loading error: "
                            + e.getMessage()
            );
        }

        return orders;
    }

    public int getOrderCountByStatus(String status) {

        String sql = """
            SELECT COUNT(*)
            FROM orders
            WHERE status = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Order count error: "
                            + e.getMessage()
            );
        }

        return 0;
    }
    public double getTotalSales() {

        String sql = """
            SELECT COALESCE(SUM(total), 0)
            FROM orders
            WHERE status != 'Cancelled'
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Total sales loading error: "
                            + e.getMessage()
            );
        }

        return 0.0;
    }
    public boolean saveBillWithOrders(
            String customerName,
            double totalAmount,
            String paymentMethod,
            ObservableList<OrderItem> orderItems) {

        String billSql = """
        INSERT INTO bills
        (customer_name, total_amount,
         payment_method, payment_status, payment_date)
        VALUES (?, ?, ?, ?, ?)
        """;

        String orderSql = """
        INSERT INTO orders
        (customer_name, coffee_name, size,
         quantity, price, total, order_date,
         status, bill_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (
                    PreparedStatement billStatement =
                            connection.prepareStatement(
                                    billSql,
                                    java.sql.Statement.RETURN_GENERATED_KEYS
                            );

                    PreparedStatement orderStatement =
                            connection.prepareStatement(orderSql)
            ) {

                // -------------------------
                // Step 1: Create Bill
                // -------------------------

                billStatement.setString(1, customerName);
                billStatement.setDouble(2, totalAmount);
                billStatement.setString(3, paymentMethod);
                billStatement.setString(4, "Paid");

                String paymentDate =
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "yyyy-MM-dd HH:mm:ss"
                                        )
                                );

                billStatement.setString(5, paymentDate);

                billStatement.executeUpdate();

                // Get generated Bill ID
                int billId;

                try (ResultSet resultSet =
                             billStatement.getGeneratedKeys()) {

                    if (!resultSet.next()) {

                        connection.rollback();

                        return false;
                    }

                    billId = resultSet.getInt(1);
                }

                // -------------------------
                // Step 2: Save Orders
                // -------------------------

                String orderDate =
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "yyyy-MM-dd HH:mm:ss"
                                        )
                                );

                for (OrderItem item : orderItems) {

                    orderStatement.setString(
                            1,
                            customerName
                    );

                    orderStatement.setString(
                            2,
                            item.getCoffeeName()
                    );

                    orderStatement.setString(
                            3,
                            item.getSize()
                    );

                    orderStatement.setInt(
                            4,
                            item.getQuantity()
                    );

                    orderStatement.setDouble(
                            5,
                            item.getPrice()
                    );

                    orderStatement.setDouble(
                            6,
                            item.getTotal()
                    );

                    orderStatement.setString(
                            7,
                            orderDate
                    );

                    orderStatement.setString(
                            8,
                            "Pending"
                    );

                    orderStatement.setInt(
                            9,
                            billId
                    );

                    orderStatement.executeUpdate();
                }

                // -------------------------
                // Step 3: Commit
                // -------------------------

                connection.commit();

                System.out.println(
                        "Bill and orders saved successfully!"
                );

                return true;

            } catch (SQLException e) {

                // Something failed
                connection.rollback();

                System.out.println(
                        "Transaction rolled back: "
                                + e.getMessage()
                );

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database transaction error: "
                            + e.getMessage()
            );

            return false;
        }
    }
    public ObservableList<CustomerOrder> getCustomerOrdersWithBilling(
            String customerName) {

        ObservableList<CustomerOrder> orderList =
                FXCollections.observableArrayList();

        String sql = """
        SELECT
            o.id AS order_id,
            o.bill_id,
            o.coffee_name,
            o.size,
            o.quantity,
            o.price,
            o.total,
            o.order_date,
            o.status AS order_status,
            b.payment_method,
            b.payment_status
        FROM orders o
        LEFT JOIN bills b
            ON o.bill_id = b.id
        WHERE o.customer_name = ?
        ORDER BY o.id DESC
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, customerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    int billId =
                            resultSet.getInt("bill_id");

                    CustomerOrder order =
                            new CustomerOrder(
                                    resultSet.getInt("order_id"),
                                    billId,
                                    resultSet.getString("coffee_name"),
                                    resultSet.getString("size"),
                                    resultSet.getInt("quantity"),
                                    resultSet.getDouble("price"),
                                    resultSet.getDouble("total"),
                                    resultSet.getString("order_date"),
                                    resultSet.getString("order_status"),
                                    resultSet.getString("payment_method"),
                                    resultSet.getString("payment_status")
                            );

                    orderList.add(order);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Customer order retrieve error: "
                            + e.getMessage()
            );
        }

        return orderList;
    }
    public boolean cancelCustomerBill(
            int billId,
            String customerName) {

        String checkSql = """
        SELECT COUNT(*) AS total_orders,
               SUM(
                   CASE
                       WHEN status = 'Pending'
                       THEN 1
                       ELSE 0
                   END
               ) AS pending_orders
        FROM orders
        WHERE bill_id = ?
          AND customer_name = ?
        """;

        String orderSql = """
        UPDATE orders
        SET status = 'Cancelled'
        WHERE bill_id = ?
          AND customer_name = ?
          AND status = 'Pending'
        """;

        String billSql = """
        UPDATE bills
        SET payment_status = 'Cancelled'
        WHERE id = ?
          AND customer_name = ?
          AND payment_status = 'Paid'
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (
                    PreparedStatement checkStatement =
                            connection.prepareStatement(checkSql);
                    PreparedStatement orderStatement =
                            connection.prepareStatement(orderSql);
                    PreparedStatement billStatement =
                            connection.prepareStatement(billSql)
            ) {

                // Check bill orders
                checkStatement.setInt(1, billId);
                checkStatement.setString(2, customerName);

                int totalOrders;
                int pendingOrders;

                try (ResultSet resultSet =
                             checkStatement.executeQuery()) {

                    if (!resultSet.next()) {
                        connection.rollback();
                        return false;
                    }

                    totalOrders =
                            resultSet.getInt("total_orders");

                    pendingOrders =
                            resultSet.getInt("pending_orders");
                }

                // Bill must contain orders
                if (totalOrders == 0) {
                    connection.rollback();
                    return false;
                }

                // Every order in the bill must be Pending
                if (pendingOrders != totalOrders) {

                    connection.rollback();

                    System.out.println(
                            "Bill cannot be cancelled because "
                                    + "one or more orders are no longer Pending."
                    );

                    return false;
                }

                // Cancel all orders
                orderStatement.setInt(1, billId);
                orderStatement.setString(2, customerName);

                orderStatement.executeUpdate();

                // Update payment status
                billStatement.setInt(1, billId);
                billStatement.setString(2, customerName);

                billStatement.executeUpdate();

                // Everything successful
                connection.commit();

                System.out.println(
                        "Bill and all related orders cancelled!"
                );

                return true;

            } catch (SQLException e) {

                connection.rollback();

                System.out.println(
                        "Cancellation transaction rolled back: "
                                + e.getMessage()
                );

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Cancellation database error: "
                            + e.getMessage()
            );

            return false;
        }
    }
    public boolean confirmOrder(int orderId) {

        String sql = """
        UPDATE orders
        SET status = 'Confirmed'
        WHERE id = ?
          AND status = 'Pending'
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Order confirmed successfully!"
                );

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Order confirmation error: "
                            + e.getMessage()
            );
        }

        return false;
    }
    public ObservableList<AdminOrder> getBaristaOrders() {

        ObservableList<AdminOrder> orders =
                FXCollections.observableArrayList();

        String sql = """
        SELECT id,
               customer_name,
               coffee_name,
               size,
               quantity,
               price,
               total,
               order_date,
               status
        FROM orders
        WHERE status = 'Confirmed'
           OR status = 'Preparing'
        ORDER BY id ASC
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AdminOrder order =
                        new AdminOrder(
                                resultSet.getInt("id"),
                                resultSet.getString("customer_name"),
                                resultSet.getString("coffee_name"),
                                resultSet.getString("size"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("price"),
                                resultSet.getDouble("total"),
                                resultSet.getString("order_date"),
                                resultSet.getString("status")
                        );

                orders.add(order);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Barista order loading error: "
                            + e.getMessage()
            );
        }

        return orders;
    }


    public boolean updateBaristaOrderStatus(
            int orderId,
            String newStatus) {

        String sql = """
        UPDATE orders
        SET status = ?
        WHERE id = ?
          AND (
              status = 'Confirmed'
              OR status = 'Preparing'
          )
        """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newStatus);
            statement.setInt(2, orderId);

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Barista order status updated successfully!"
                );

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Barista status update error: "
                            + e.getMessage()
            );
        }

        return false;
    }
}
