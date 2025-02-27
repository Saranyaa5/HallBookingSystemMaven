package com.hallbooking.dao;

import com.hallbooking.model.Payment;
import com.hallbooking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDAO {
    
    public void displayUserBookings(int userId) {
        String query = "SELECT B.BOOKING_ID, H.HALL_NAME, B.BOOKING_DATE, P.AMOUNT, P.STATUS " +
                       "FROM BOOKING B " +
                       "JOIN HALLS H ON B.HALL_ID = H.HALL_ID " +
                       "JOIN PAYMENT P ON B.BOOKING_ID = P.BOOKING_ID " +
                       "WHERE B.USER_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nYour Booked Halls:");
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("BOOKING_ID") +
                        ", Hall: " + rs.getString("HALL_NAME") +
                        ", Date: " + rs.getDate("BOOKING_DATE") +
                        ", Amount: ₹" + rs.getDouble("AMOUNT") +
                        ", Status: " + rs.getString("STATUS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidBookingForUser(int userId, int bookingId) {
        String query = "SELECT COUNT(*) FROM BOOKING WHERE USER_ID = ? AND BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getPaymentAmount(int bookingId) {
        String query = "SELECT AMOUNT FROM PAYMENT WHERE BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("AMOUNT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean processPayment(int bookingId, String paymentMode) {
        String query = "UPDATE PAYMENT SET PAYMENT_MODE = ?, STATUS = 'COMPLETED' WHERE BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, paymentMode);
            stmt.setInt(2, bookingId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
