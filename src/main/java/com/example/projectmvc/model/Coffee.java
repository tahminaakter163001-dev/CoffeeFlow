package com.example.projectmvc.model;

import javafx.beans.property.*;

public class Coffee {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty size;
    private final DoubleProperty price;

    public Coffee(int id, String name, String size, double price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
        this.price = new SimpleDoubleProperty(price);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }
}
