package com.example.projectmvc.model;

import javafx.beans.property.*;

public class CustomerOrder {

    private final IntegerProperty orderId;
    private final IntegerProperty billId;
    private final StringProperty coffeeName;
    private final StringProperty size;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty total;
    private final StringProperty orderDate;
    private final StringProperty orderStatus;
    private final StringProperty paymentMethod;
    private final StringProperty paymentStatus;

    public CustomerOrder(
            int orderId,
            int billId,
            String coffeeName,
            String size,
            int quantity,
            double price,
            double total,
            String orderDate,
            String orderStatus,
            String paymentMethod,
            String paymentStatus) {

        this.orderId =
                new SimpleIntegerProperty(orderId);

        this.billId =
                new SimpleIntegerProperty(billId);

        this.coffeeName =
                new SimpleStringProperty(coffeeName);

        this.size =
                new SimpleStringProperty(size);

        this.quantity =
                new SimpleIntegerProperty(quantity);

        this.price =
                new SimpleDoubleProperty(price);

        this.total =
                new SimpleDoubleProperty(total);

        this.orderDate =
                new SimpleStringProperty(orderDate);

        this.orderStatus =
                new SimpleStringProperty(orderStatus);

        this.paymentMethod =
                new SimpleStringProperty(paymentMethod);

        this.paymentStatus =
                new SimpleStringProperty(paymentStatus);
    }

    public int getOrderId() {
        return orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public int getBillId() {
        return billId.get();
    }

    public IntegerProperty billIdProperty() {
        return billId;
    }

    public String getCoffeeName() {
        return coffeeName.get();
    }

    public StringProperty coffeeNameProperty() {
        return coffeeName;
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public StringProperty orderDateProperty() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus.get();
    }

    public StringProperty orderStatusProperty() {
        return orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus.get();
    }

    public StringProperty paymentStatusProperty() {
        return paymentStatus;
    }
}