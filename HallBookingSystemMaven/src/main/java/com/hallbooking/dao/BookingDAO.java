package com.hallbooking.dao;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import com.hallbooking.utils.DBConnection;

public class BookingDAO {

    
	public static boolean isHallBooked(String hallId, Date bookingDate) {
        String query = "SELECT COUNT(*) FROM HALLBOOKINGSYSTEM.booking WHERE hall_id = ? AND booking_date = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, hallId);
            pstmt.setDate(2, bookingDate);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

	public static void displayHallBookings() {
	    String query = "SELECT h.hall_id, h.hall_name, b.booking_date FROM HALLBOOKINGSYSTEM.halls h " +
	                   "LEFT JOIN HALLBOOKINGSYSTEM.booking b ON h.hall_id = b.hall_id " +
	                   "ORDER BY h.hall_id, b.booking_date";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {

	        System.out.println("\nHall Bookings:");
	        System.out.println("----------------------------------------");
	        System.out.printf("%-10s %-20s %-15s\n", "Hall ID", "Hall Name", "Booked Date");
	        System.out.println("----------------------------------------");

	        while (rs.next()) {
	            String hallId = rs.getString("hall_id");
	            String hallName = rs.getString("hall_name");
	            Date bookingDate = rs.getDate("booking_date");

	            String dateStr = (bookingDate != null) ? bookingDate.toString() : "Available";
	            System.out.printf("%-10s %-20s %-15s\n", hallId, hallName, dateStr);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    public static boolean bookhall(int userId, String hallId, Date bookingDate) {
        String query = "INSERT INTO HALLBOOKINGSYSTEM.booking (booking_id, user_id, hall_id, booking_date) " +
                       "VALUES (booking_id_sequence.NEXTVAL, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, hallId);
            pstmt.setDate(3, bookingDate);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void displayBookingsbyUser(int userId) {
        String query = "SELECT booking_id, hall_id, booking_date FROM HALLBOOKINGSYSTEM.booking WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nYour Bookings:");
            System.out.println("-----------------------------------");
            System.out.printf("%-10s %-10s %-15s\n", "Booking ID", "Hall ID", "Booking Date");
            System.out.println("-----------------------------------");

            boolean hasBookings = false;
            while (rs.next()) {
                hasBookings = true;
                int bookingId = rs.getInt("booking_id");
                String hallId = rs.getString("hall_id");
                Date bookingDate = rs.getDate("booking_date");

                System.out.printf("%-10d %-10s %-15s\n", bookingId, hallId, bookingDate);
            }

            if (!hasBookings) {
                System.out.println("No bookings found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean cancelBooking(int bookingId, int userId) {
        String checkQuery = "SELECT user_id FROM HALLBOOKINGSYSTEM.booking WHERE booking_id = ?";
        String deleteQuery = "DELETE FROM HALLBOOKINGSYSTEM.booking WHERE booking_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

            checkStmt.setInt(1, bookingId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int ownerUserId = rs.getInt("user_id");

                if (ownerUserId != userId) {
                    System.out.println("Cancellation failed. You can only cancel your own bookings.");
                    return false;
                }
                deleteStmt.setInt(1, bookingId);
                deleteStmt.setInt(2, userId);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                System.out.println("Cancellation failed. Booking ID not found.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    
}