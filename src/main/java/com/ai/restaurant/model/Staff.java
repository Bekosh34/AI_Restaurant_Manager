package com.ai.restaurant.model;

public class Staff {
    private int id;
    private String name;
    private String role;
    private int salary;

    public Staff(int id, String name, String role, int salary) {
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

    public int getSalary() {
        return salary;
    }
}
