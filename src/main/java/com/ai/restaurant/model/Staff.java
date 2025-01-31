package com.ai.restaurant.model;

public class Staff {
    private int id;
    private String name;
    private String role;
    private double salary;

    public Staff(int id, String name, String role) {
        this(id, name, role, 0.0); // Default salary set to 0.0
    }

    public Staff(int id, String name, String role, double salary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    public void setRole(String role) {
        this.role = role;
    }
}