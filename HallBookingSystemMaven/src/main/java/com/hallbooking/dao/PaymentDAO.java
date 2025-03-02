package com.hallbooking.dao;

import com.hallbooking.utils.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentDAO {

	public void displayUserBookings(int userId) {
        String query = "SELECT B.BOOKING_ID, H.HALL_NAME, B.BOOKING_DATE, P.AMOUNT, P.STATUS " +
                       "FROM HALLBOOKINGSYSTEM.BOOKING B " +
                       "JOIN HALLS H ON B.HALL_ID = H.HALL_ID " +
                       "JOIN HALLBOOKINGSYSTEM.PAYMENT P ON B.BOOKING_ID = P.BOOKING_ID " +
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
        String query = "SELECT COUNT(*) FROM HALLBOOKINGSYSTEM.BOOKING WHERE USER_ID = ? AND BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookingId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getPaymentAmount(int bookingId) {
        String query = "SELECT AMOUNT FROM HALLBOOKINGSYSTEM.PAYMENT WHERE BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble("AMOUNT") : 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public String getExistingUPI(int userId) {
        String query = "SELECT UPI_ID FROM HALLBOOKINGSYSTEM.UPI_PAYMENT WHERE USER_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("UPI_ID") : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	    public boolean processUPIPayment(int bookingId, int userId, String paymentMode, String paymentDetails) {
	        Connection conn = null;
	        PreparedStatement updatePaymentStmt = null;
	        PreparedStatement insertDetailStmt = null;
	        ResultSet rs = null;

	        try {
	            conn = DBConnection.getConnection();
	            conn.setAutoCommit(false);

	            String checkPaymentStatusQuery = "SELECT PAYMENT_ID, STATUS FROM HALLBOOKINGSYSTEM.PAYMENT WHERE BOOKING_ID = ?";
	            try (PreparedStatement checkStmt = conn.prepareStatement(checkPaymentStatusQuery)) {
	                checkStmt.setInt(1, bookingId);
	                rs = checkStmt.executeQuery();
	                if (!rs.next() || "COMPLETED".equalsIgnoreCase(rs.getString("STATUS"))) {
	                    System.err.println("Error: Payment already completed or not found.");
	                    return false;
	                }

	                int paymentId = rs.getInt("PAYMENT_ID");

	                String updatePaymentQuery = "UPDATE HALLBOOKINGSYSTEM.PAYMENT SET PAYMENT_MODE = ?, STATUS = 'COMPLETED' WHERE BOOKING_ID = ?";
	                updatePaymentStmt = conn.prepareStatement(updatePaymentQuery);
	                updatePaymentStmt.setString(1, paymentMode);
	                updatePaymentStmt.setInt(2, bookingId);
	                updatePaymentStmt.executeUpdate();

	                if ("UPI".equalsIgnoreCase(paymentMode)) {
	                    String insertUPIQuery = "INSERT INTO HALLBOOKINGSYSTEM.UPI_PAYMENT (PAYMENT_ID, USER_ID, UPI_ID) VALUES (?, ?, ?)";
	                    insertDetailStmt = conn.prepareStatement(insertUPIQuery);
	                    insertDetailStmt.setInt(1, paymentId);
	                    insertDetailStmt.setInt(2, userId);
	                    insertDetailStmt.setString(3, paymentDetails);
	                } else {
	                    System.err.println("Invalid payment mode!");
	                    return false;
	                }

	                insertDetailStmt.executeUpdate();
	                conn.commit();
	                return true;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        } finally {
	            try { if (rs != null) rs.close(); if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
	        }
	        return false;
	    }
	    public Map<String, String> getExistingCardDetails(int userId) {
	        String query = "SELECT CARD_NUMBER, EXPIRY_DATE, CVV FROM (" +
	                       "SELECT CARD_NUMBER, EXPIRY_DATE, CVV, " +
	                       "ROW_NUMBER() OVER (ORDER BY PAYMENT_ID DESC) AS rn " +
	                       "FROM HALLBOOKINGSYSTEM.CREDIT_DEBIT_PAYMENT " +
	                       "WHERE USER_ID = ? ) " +
	                       "WHERE rn = 1";

	        Map<String, String> cardDetails = new HashMap<>();

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, userId);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                cardDetails.put("cardNumber", rs.getString("CARD_NUMBER"));
	                cardDetails.put("expiryDate", rs.getString("EXPIRY_DATE"));
	                cardDetails.put("cvv", rs.getString("CVV"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return cardDetails;
	    }




	    public boolean processCardPayment(int bookingId, int userId, String cardNumber, String expiryDate, String cvv, String paymentMode) {
	        Connection conn = null;
	        PreparedStatement updatePaymentStmt = null;
	        PreparedStatement insertCardStmt = null;
	        ResultSet rs = null;

	        try {
	            conn = DBConnection.getConnection();
	            conn.setAutoCommit(false);

	            
	            String checkPaymentQuery = "SELECT PAYMENT_ID, STATUS FROM HALLBOOKINGSYSTEM.PAYMENT WHERE BOOKING_ID = ?";
	            try (PreparedStatement checkStmt = conn.prepareStatement(checkPaymentQuery)) {
	                checkStmt.setInt(1, bookingId);
	                rs = checkStmt.executeQuery();

	                if (!rs.next()) {
	                    System.err.println("❌ Payment record not found for booking ID: " + bookingId);
	                    return false;
	                }

	                int paymentId = rs.getInt("PAYMENT_ID");
	                String status = rs.getString("STATUS");

	                if ("COMPLETED".equalsIgnoreCase(status)) {
	                    System.err.println("⚠️ Payment already completed for this booking.");
	                    return false;
	                }

	                
	                String updatePaymentQuery = "UPDATE HALLBOOKINGSYSTEM.PAYMENT SET PAYMENT_MODE = ?, STATUS = 'COMPLETED' WHERE PAYMENT_ID = ?";
	                updatePaymentStmt = conn.prepareStatement(updatePaymentQuery);
	                updatePaymentStmt.setString(1, paymentMode);
	                updatePaymentStmt.setInt(2, paymentId);
	                updatePaymentStmt.executeUpdate();

	                String insertCardQuery = "INSERT INTO HALLBOOKINGSYSTEM.CREDIT_DEBIT_PAYMENT (PAYMENT_ID, USER_ID, CARD_NUMBER, EXPIRY_DATE, CVV) VALUES (?, ?, ?, ?, ?)";
	                insertCardStmt = conn.prepareStatement(insertCardQuery);
	                insertCardStmt.setInt(1, paymentId);
	                insertCardStmt.setInt(2, userId);
	                insertCardStmt.setString(3, cardNumber);
	                insertCardStmt.setString(4, expiryDate);
	                insertCardStmt.setString(5, cvv);
	                insertCardStmt.executeUpdate();

	                conn.commit();
	                System.out.println("\n✅ Payment Successful! Your hall booking is confirmed.");
	                return true;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            if (conn != null) {
	                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	            }
	        } finally {
	            try {
	                if (rs != null) rs.close();
	                if (updatePaymentStmt != null) updatePaymentStmt.close();
	                if (insertCardStmt != null) insertCardStmt.close();
	                if (conn != null) conn.setAutoCommit(true);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        System.err.println("❌ Payment Failed! Please try again.");
	        return false;
	    }
}