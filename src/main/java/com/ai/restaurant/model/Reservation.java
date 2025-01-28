package com.ai.restaurant.model;

public class Reservation {
    private int id;
    private String customerName;
    private String date;
    private int tableNumber; // Ensure this matches the database structure

    public Reservation(int id, String customerName, String date, int tableNumber) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.tableNumber = tableNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
