package com.ai.restaurant.database;

import java.sql.Connection;

public class DatabaseTest {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.connect()) {
            if (conn != null) {
                System.out.println("Connection to SQLite has been established!");
            } else {
                System.out.println("Failed to connect to SQLite!");
            }
        } catch (Exception e) {  // Catching SQLException or other exceptions
            e.printStackTrace();
        }
    }
}
