package com.ai.restaurant.model;

import javafx.beans.property.*;

public class Reservation {
    private final IntegerProperty id;
    private final StringProperty customerName;
    private final StringProperty date;
    private final IntegerProperty tableNumber;

    public Reservation(int id, String customerName, String date, int tableNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.customerName = new SimpleStringProperty(customerName);
        this.date = new SimpleStringProperty(date);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getCustomerName() { return customerName.get(); }
    public StringProperty customerNameProperty() { return customerName; }

    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }

    public int getTableNumber() { return tableNumber.get(); }
    public IntegerProperty tableNumberProperty() { return tableNumber; }
}
