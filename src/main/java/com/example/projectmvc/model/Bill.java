package com.example.projectmvc.model;

import javafx.beans.property.*;

public class Bill {

    private final IntegerProperty id;
    private final StringProperty customerName;
    private final DoubleProperty totalAmount;
    private final StringProperty paymentMethod;
    private final StringProperty paymentStatus;
    private final StringProperty paymentDate;

    public Bill(
            int id,
            String customerName,
            double totalAmount,
            String paymentMethod,
            String paymentStatus,
            String paymentDate) {

        this.id =
                new SimpleIntegerProperty(id);

        this.customerName =
                new SimpleStringProperty(customerName);

        this.totalAmount =
                new SimpleDoubleProperty(totalAmount);

        this.paymentMethod =
                new SimpleStringProperty(paymentMethod);

        this.paymentStatus =
                new SimpleStringProperty(paymentStatus);

        this.paymentDate =
                new SimpleStringProperty(paymentDate);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
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

    public String getPaymentDate() {
        return paymentDate.get();
    }

    public StringProperty paymentDateProperty() {
        return paymentDate;
    }
}