package com.ai.restaurant.model;

public class Staff {
    private final int id;
    private final String staffName;
    private final String role;

    public Staff(int id, String staffName, String role) {
        this.id = id;
        this.staffName = staffName;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getRole() {
        return role;
    }
}
