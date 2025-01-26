package com.ai.restaurant.model;

public class Reservation {
    private int id;
    private String customerName;
    private String date;
    private int tableNumber;

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
