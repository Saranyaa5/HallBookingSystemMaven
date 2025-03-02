package com.hallbooking.dao;

import com.hallbooking.model.Hall;

import com.hallbooking.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String checkQuery = "SELECT * FROM HALLBOOKINGSYSTEM.booking WHERE hall_id = ?";
        String deleteQuery = "DELETE FROM HALLBOOKINGSYSTEM.hkalls WHERE hall_id = ?";
        
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
    
    public static double calculateAmount(String hallId) {
        String query = "SELECT capacity, amenities FROM HALLBOOKINGSYSTEM.halls WHERE hall_id = ?";
        double basePricePerPerson = 50.0; 

        Map<String, Double> amenityCharges = new HashMap<>();
        amenityCharges.put("AC", 10000.0);
        amenityCharges.put("PARKING", 5000.0);
        amenityCharges.put("WIFI", 2000.0);
        amenityCharges.put("PROJECTOR", 1000.0);
        amenityCharges.put("SOUND_SYSTEM", 500.0);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, hallId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                String amenities = rs.getString("amenities");

                double totalAmount = capacity * basePricePerPerson;

               
                if (amenities != null) {
                    for (String amenity : amenityCharges.keySet()) {
                        if (amenities.toUpperCase().contains(amenity)) {
                            totalAmount += amenityCharges.get(amenity);
                        }
                    }
                }

                return totalAmount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; 
    }



}





















