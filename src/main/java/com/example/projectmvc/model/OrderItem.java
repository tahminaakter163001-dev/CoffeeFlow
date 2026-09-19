package com.example.projectmvc.model;

import javafx.beans.property.*;

public class OrderItem {

    private final StringProperty coffeeName;
    private final StringProperty size;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty total;

    public OrderItem(
            String coffeeName,
            String size,
            int quantity,
            double price) {

        this.coffeeName =
                new SimpleStringProperty(coffeeName);

        this.size =
                new SimpleStringProperty(size);

        this.quantity =
                new SimpleIntegerProperty(quantity);

        this.price =
                new SimpleDoubleProperty(price);

        this.total =
                new SimpleDoubleProperty(
                        quantity * price
                );
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
}

