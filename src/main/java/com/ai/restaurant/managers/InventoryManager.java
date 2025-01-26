package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    public static boolean addInventoryItem(Inventory inventory) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO inventory (item_name, quantity) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, inventory.getItemName());
            statement.setInt(2, inventory.getQuantity());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Inventory> getAllInventoryItems() {
        List<Inventory> inventoryList = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM inventory";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Inventory inventory = new Inventory(
                        resultSet.getInt("id"),
                        resultSet.getString("item_name"),
                        resultSet.getInt("quantity")
                );
                inventoryList.add(inventory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }

    public static boolean updateInventoryQuantity(int itemId, int newQuantity) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "UPDATE inventory SET quantity = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newQuantity);
            statement.setInt(2, itemId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteInventoryItem(int itemId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM inventory WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, itemId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
