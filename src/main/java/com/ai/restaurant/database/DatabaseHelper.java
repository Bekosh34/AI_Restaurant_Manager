package com.ai.restaurant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:restaurant.db"; // Change to your actual DB path
    private static final String DRIVER = "org.sqlite.JDBC";

    public static void main(String[] args) {
        try (Connection conn = DatabaseHelper.connect()) {
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static {
        try {
            Class.forName(DRIVER); // Ensure SQLite driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Database driver not found!");
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }
}
