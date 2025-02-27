package com.hallbooking.dao;

import com.hallbooking.model.Hall;
import com.hallbooking.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HallDAO {

    public boolean addHall(Hall hall) {
        String query = "INSERT INTO HALLBOOKINGSYSTEM.halls (hall_id, hall_name, capacity, amenities, location) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hall.getHallId());
            stmt.setString(2, hall.getHallName());
            stmt.setInt(3, hall.getCapacity());
            stmt.setString(4, hall.getAmenities());
            stmt.setString(5, hall.getLocation());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHall(String hallId) {
        String checkQuery = "SELECT * FROM HALLBOOKINGSYSTEM.bookings WHERE hall_id = ?";
        String deleteQuery = "DELETE FROM HALLBOOKINGSYSTEM.halls WHERE hall_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            
            checkStmt.setString(1, hallId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Cannot delete hall. It has active bookings.");
                return false;
            }
            
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, hallId);
                return deleteStmt.executeUpdate() > 0;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Hall> getAllHalls() {
        List<Hall> halls = new ArrayList<>();
        String query = "SELECT * FROM HALLBOOKINGSYSTEM.halls";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Hall hall = new Hall(
                    rs.getString("hall_id"),
                    rs.getString("hall_name"),
                    rs.getInt("capacity"),
                    rs.getString("amenities"),
                    rs.getString("location")
                );
                halls.add(hall);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving halls: " + e.getMessage());
        }
        return halls;
    }
}
