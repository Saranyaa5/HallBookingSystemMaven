
package com.hallbooking.dao;

import com.hallbooking.model.Hall;
import com.hallbooking.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HallDAO {

    public Hall searchByHallId(String hallId) {
        String query = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE hall_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, hallId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Hall(
                        rs.getString("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("capacity"),
                        rs.getString("amenities"),
                        rs.getString("location")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching hall by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null; 
    }
    public List<Hall> searchByName(String name) {
        List<Hall> halls = new ArrayList<>();
        String query = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE hall_name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                halls.add(new Hall(
                        rs.getString("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("capacity"),
                        rs.getString("amenities"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching halls by name: " + e.getMessage());
            e.printStackTrace();
        }
        return halls;
    }
    public List<Hall> getAllHalls() {
        List<Hall> halls = new ArrayList<>();
        String query = "SELECT * FROM HALLBOOKINGSYSTEM.halls";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                halls.add(new Hall(
                        rs.getString("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("capacity"),
                        rs.getString("amenities"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all halls: " + e.getMessage());
            e.printStackTrace();
        }
        return halls;
    }
}