package com.example.projectmvc.model;

import javafx.beans.property.*;

public class OrderHistory {

    private final IntegerProperty id;
    private final StringProperty coffeeName;
    private final StringProperty size;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty total;
    private final StringProperty orderDate;
    private final StringProperty status;

    public OrderHistory(
            int id,
            String coffeeName,
            String size,
            int quantity,
            double price,
            double total,
            String orderDate,
            String status) {

        this.id =
                new SimpleIntegerProperty(id);

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

        this.status =
                new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
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

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }
}