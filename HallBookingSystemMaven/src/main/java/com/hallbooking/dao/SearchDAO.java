package com.hallbooking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.hallbooking.utils.DBConnection;
import com.hallbooking.ConsoleColors;

public class SearchDAO {

    public void searchHallsByDate(Date bookingDate) {
        String query = "SELECT h.* FROM HALLBOOKINGSYSTEM.halls h " +
                       "LEFT JOIN HALLBOOKINGSYSTEM.booking b ON h.hall_id = b.hall_id AND b.booking_date = ? " +
                       "WHERE b.hall_id IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, bookingDate);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nAvailable Halls on " + bookingDate + ":");
            printHallResults(rs);

        } catch (SQLException e) {
            System.out.println("Error fetching available halls: " + e.getMessage());
        }
    }

   
    public void searchHallById(String id) {
        String hallQuery = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE hall_id = ?";
        String bookingQuery = "SELECT hall_id, booking_date FROM HALLBOOKINGSYSTEM.booking WHERE hall_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement hallStmt = conn.prepareStatement(hallQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

            hallStmt.setString(1, id);
            ResultSet hallRs = hallStmt.executeQuery();

            System.out.println("\nHall Details for ID: " + id);
            printHallResults(hallRs);

            bookingStmt.setString(1, id);
            ResultSet bookingRs = bookingStmt.executeQuery();
            printBookingResults(bookingRs, id);

        } catch (SQLException e) {
            System.out.println("Error fetching hall details: " + e.getMessage());
        }
    }


    public void searchHallByName(String name) {
        String hallQuery = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE LOWER(hall_name) = LOWER(?)";
        String bookingQuery = "SELECT hall_id, booking_date FROM HALLBOOKINGSYSTEM.booking WHERE hall_id IN " +
                              "(SELECT hall_id FROM HALLBOOKINGSYSTEM.halls WHERE LOWER(hall_name) = LOWER(?))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement hallStmt = conn.prepareStatement(hallQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

            hallStmt.setString(1, name);
            ResultSet hallRs = hallStmt.executeQuery();

            System.out.println("\nHalls with Name: " + name);
            printHallResults(hallRs);

            bookingStmt.setString(1, name);
            ResultSet bookingRs = bookingStmt.executeQuery();
            printBookingResults(bookingRs, name);

        } catch (SQLException e) {
            System.out.println("Error fetching hall details: " + e.getMessage());
        }
    }

    public void searchHallByCapacity(int capacity) {
        String hallQuery = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE capacity <= ?";
        String bookingQuery = "SELECT hall_id, booking_date FROM HALLBOOKINGSYSTEM.booking WHERE hall_id IN " +
                              "(SELECT hall_id FROM HALLBOOKINGSYSTEM.halls WHERE capacity <= ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement hallStmt = conn.prepareStatement(hallQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

            hallStmt.setInt(1, capacity);
            ResultSet hallRs = hallStmt.executeQuery();

            System.out.println("\nHalls with Capacity <= " + capacity);
            printHallResults(hallRs);

            bookingStmt.setInt(1, capacity);
            ResultSet bookingRs = bookingStmt.executeQuery();
            printBookingResults(bookingRs, "Capacity <= " + capacity);

        } catch (SQLException e) {
            System.out.println("Error fetching hall details: " + e.getMessage());
        }
    }

    public void searchHallByLocation(String location) {
        String hallQuery = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE LOWER(location) = LOWER(?)";
        String bookingQuery = "SELECT hall_id, booking_date FROM HALLBOOKINGSYSTEM.booking WHERE hall_id IN " +
                              "(SELECT hall_id FROM HALLBOOKINGSYSTEM.halls WHERE LOWER(location) = LOWER(?))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement hallStmt = conn.prepareStatement(hallQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

            hallStmt.setString(1, location);
            ResultSet hallRs = hallStmt.executeQuery();

            System.out.println("\nHalls in Location: " + location);
            printHallResults(hallRs);

            bookingStmt.setString(1, location);
            ResultSet bookingRs = bookingStmt.executeQuery();
            printBookingResults(bookingRs, location);

        } catch (SQLException e) {
            System.out.println("Error fetching hall details: " + e.getMessage());
        }
    }

    public void searchHallByAmenities(String amenities) {
        String hallQuery = "SELECT * FROM HALLBOOKINGSYSTEM.halls WHERE LOWER(amenities) LIKE LOWER(?)";
        String bookingQuery = "SELECT hall_id, booking_date FROM HALLBOOKINGSYSTEM.booking WHERE hall_id IN " +
                              "(SELECT hall_id FROM HALLBOOKINGSYSTEM.halls WHERE LOWER(amenities) LIKE LOWER(?))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement hallStmt = conn.prepareStatement(hallQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

            hallStmt.setString(1, "%" + amenities + "%");
            ResultSet hallRs = hallStmt.executeQuery();

            System.out.println("\nHalls with Amenities: " + amenities);
            printHallResults(hallRs);

            bookingStmt.setString(1, "%" + amenities + "%");
            ResultSet bookingRs = bookingStmt.executeQuery();
            printBookingResults(bookingRs, amenities);

        } catch (SQLException e) {
            System.out.println("Error fetching hall details: " + e.getMessage());
        }
    }

    private void printHallResults(ResultSet rs) throws SQLException {
        boolean found = false;
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-15s %-20s\n", "Hall ID", "Hall Name", "Capacity", "Location", "Amenities");
        System.out.println("-----------------------------------------------------------");

        while (rs.next()) {
            found = true;
            System.out.printf("%-10s %-20s %-10d %-15s %-20s\n",
                    rs.getString("hall_id"),
                    rs.getString("hall_name"),
                    rs.getInt("capacity"),
                    rs.getString("location"),
                    rs.getString("amenities"));
        }

        if (!found) {
            System.out.println("No halls found.");
        }
        System.out.println("-----------------------------------------------------------");
    }

    private void printBookingResults(ResultSet rs, String searchCriteria) throws SQLException {
        boolean found = false;
        System.out.println("\nBooking Dates of above available halls: " + searchCriteria);
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-10s %-15s\n", "Hall ID", "Booking Date");
        System.out.println("-----------------------------------------------------------");

      
        while (rs.next()) {
            found = true;
            System.out.printf("%-10s %-15s\n",
                    rs.getString("hall_id"),  
                    rs.getDate("booking_date"));
        }

        if (!found) {
            System.out.println("No bookings found.");
        }
        System.out.println("-----------------------------------------------------------");
    }
    
    public void searchAllHalls() {
        String query = "SELECT * FROM HALLBOOKINGSYSTEM.halls";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\nList of All Halls:");
            printHallResults(rs);

            String bookingQuery = "SELECT * FROM HALLBOOKINGSYSTEM.booking";
            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
                 ResultSet bookingRs = bookingStmt.executeQuery()) {

                System.out.println("\nBooking Details:");
                printBookingResults(bookingRs,"all halls");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching halls and bookings: " + e.getMessage());
        }
    }

    

    

}
