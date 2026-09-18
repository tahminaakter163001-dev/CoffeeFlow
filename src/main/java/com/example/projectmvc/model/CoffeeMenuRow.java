package com.example.projectmvc.model;

import javafx.beans.property.*;

public class CoffeeMenuRow {

    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty smallPrice;
    private final DoubleProperty mediumPrice;
    private final DoubleProperty largePrice;

    public CoffeeMenuRow(int id, String name,
                         double smallPrice,
                         double mediumPrice,
                         double largePrice) {

        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.smallPrice = new SimpleDoubleProperty(smallPrice);
        this.mediumPrice = new SimpleDoubleProperty(mediumPrice);
        this.largePrice = new SimpleDoubleProperty(largePrice);
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

    public double getSmallPrice() {
        return smallPrice.get();
    }

    public DoubleProperty smallPriceProperty() {
        return smallPrice;
    }

    public double getMediumPrice() {
        return mediumPrice.get();
    }

    public DoubleProperty mediumPriceProperty() {
        return mediumPrice;
    }

    public double getLargePrice() {
        return largePrice.get();
    }

    public DoubleProperty largePriceProperty() {
        return largePrice;
    }
}


