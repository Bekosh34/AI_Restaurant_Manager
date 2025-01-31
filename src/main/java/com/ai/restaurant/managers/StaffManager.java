package com.ai.restaurant.managers;

import com.ai.restaurant.model.Staff;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {
    private static List<Staff> staffList = new ArrayList<>();

    public static void addStaff(Staff staff) {
        staffList.add(staff);
    }

    public static void deleteStaff(int id) {
        staffList.removeIf(staff -> staff.getId() == id);
    }

    public static List<Staff> getAllStaff() {
        return new ArrayList<>(staffList);
    }

    public static void updateStaff(Staff staff) {
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getId() == staff.getId()) {
                staffList.set(i, staff);
                break;
            }
        }
    }
}