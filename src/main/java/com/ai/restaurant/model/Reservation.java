package com.ai.restaurant.model;

public class Reservation {
    private final int id;
    private final String customerName;
    private final String date;
    private final int tableNumber;

    public Reservation(int id, String customerName, String date, int tableNumber) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.tableNumber = tableNumber;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDate() {
        return date;
    }

    public int getTableNumber() {
        return tableNumber;
    }
}
