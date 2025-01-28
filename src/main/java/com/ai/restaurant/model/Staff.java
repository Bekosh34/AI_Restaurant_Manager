package com.ai.restaurant.model;

public class Staff {
    private int id;
    private String name;
    private String role;
    private double salary; // ✅ Ensure salary is included

    // ✅ Constructor with salary
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

    public double getSalary() { // ✅ Ensure this method exists
        return salary;
    }
}
