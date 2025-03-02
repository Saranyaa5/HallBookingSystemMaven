package com.hallbooking.dao;

import com.hallbooking.utils.DBConnection;
import java.sql.*;
import java.util.HashMap;
import com.hallbooking.ConsoleColors;
import java.util.Map;
import com.hallbooking.utils.EmailUtil;

public class PaymentDAO {

	public void displayUserBookings(int userId) {
        String query = "SELECT B.BOOKING_ID, H.HALL_NAME, B.BOOKING_DATE, P.AMOUNT, P.STATUS " +
                       "FROM HALLBOOKINGSYSTEM.BOOKING B " +
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
        String query = "SELECT AMOUNT FROM PAYMENT WHERE BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("AMOUNT");
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment amount: " + e.getMessage());
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
    
    public Map<String, String> getExistingCardDetails(int userId) {
        Map<String, String> cardDetails = new HashMap<>();
        String query = "SELECT CARD_NUMBER, EXPIRY_DATE, CVV FROM HALLBOOKINGSYSTEM.CREDIT_DEBIT_PAYMENT WHERE USER_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

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

    public boolean processUPIPayment(int bookingId, int userId, String paymentMode, String paymentDetails) {
        Connection conn = null;
        PreparedStatement updatePaymentStmt = null;
        PreparedStatement insertDetailStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

           
            String customerEmail = getCustomerEmail(userId);
            if (customerEmail == null) {
                System.err.println("Customer email not found!");
                return false;
            }

           
            String adminEmail = "svgfamily3@gmail.com";

            
            String checkPaymentStatusQuery = "SELECT PAYMENT_ID, STATUS FROM PAYMENT WHERE BOOKING_ID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkPaymentStatusQuery)) {
                checkStmt.setInt(1, bookingId);
                rs = checkStmt.executeQuery();
                if (!rs.next() || "COMPLETED".equalsIgnoreCase(rs.getString("STATUS"))) {
                    System.err.println("Error: Payment already completed or not found.");
                    return false;
                }

                int paymentId = rs.getInt("PAYMENT_ID");

                
                String updatePaymentQuery = "UPDATE PAYMENT SET PAYMENT_MODE = ?, STATUS = 'COMPLETED' WHERE BOOKING_ID = ?";
                updatePaymentStmt = conn.prepareStatement(updatePaymentQuery);
                updatePaymentStmt.setString(1, paymentMode);
                updatePaymentStmt.setInt(2, bookingId);
                updatePaymentStmt.executeUpdate();

        
                if ("UPI".equalsIgnoreCase(paymentMode)) {
                    String insertUPIQuery = "INSERT INTO UPI_PAYMENT (PAYMENT_ID, USER_ID, UPI_ID) VALUES (?, ?, ?)";
                    insertDetailStmt = conn.prepareStatement(insertUPIQuery);
                    insertDetailStmt.setInt(1, paymentId);
                    insertDetailStmt.setInt(2, userId);
                    insertDetailStmt.setString(3, paymentDetails);
                    insertDetailStmt.executeUpdate();
                } else {
                    System.err.println("Invalid payment mode!");
                    return false;
                }

                conn.commit();

                // Send confirmation emails
                sendConfirmationEmails(customerEmail, adminEmail, bookingId, userId);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (updatePaymentStmt != null) updatePaymentStmt.close();
                if (insertDetailStmt != null) insertDetailStmt.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getCustomerEmail(int userId) {
        String query = "SELECT EMAIL FROM CUSTOMER WHERE USER_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("EMAIL");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    

    private void sendConfirmationEmails(String customerEmail, String adminEmail, int bookingId, int userId) {
   
        String bookingDetails = getBookingDetails(bookingId);

        String customerSubject = "Hall Booking Confirmation";
        String customerBody = "Dear Customer,\n\n" +
                              "Your hall booking has been confirmed.\n\n" +
                              "Booking Details:\n" + bookingDetails +
                              "\n\nThank you for using our service!\n\n" +
                              "Best regards,\nHall Booking System Team";

     
        String adminSubject = "New Hall Booking Confirmation";
        String adminBody = "Dear Admin,\n\n" +
                           "A new hall booking has been confirmed.\n\n" +
                           "Booking Details:\n" + bookingDetails +
                           "\n\nBest regards,\nHall Booking System Team";

        EmailUtil.sendEmail(customerEmail, customerSubject, customerBody);
        EmailUtil.sendEmail(adminEmail, adminSubject, adminBody);
    }

    private String getBookingDetails(int bookingId) {
        String query = "SELECT B.BOOKING_ID, H.HALL_NAME, B.BOOKING_DATE, P.AMOUNT " +
                       "FROM BOOKING B " +
                       "JOIN HALLS H ON B.HALL_ID = H.HALL_ID " +
                       "JOIN PAYMENT P ON B.BOOKING_ID = P.BOOKING_ID " +
                       "WHERE B.BOOKING_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "Booking ID: " + rs.getInt("BOOKING_ID") + "\n" +
                       "Hall Name: " + rs.getString("HALL_NAME") + "\n" +
                       "Booking Date: " + rs.getDate("BOOKING_DATE") + "\n" +
                       "Amount: ₹" + rs.getDouble("AMOUNT");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching booking details: " + e.getMessage());
            e.printStackTrace();
        }
        return "No details found.";
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
	                    System.err.println("Payment record not found for booking ID: " + bookingId);
	                    return false;
	                }

	                int paymentId = rs.getInt("PAYMENT_ID");
	                String status = rs.getString("STATUS");

	                if ("COMPLETED".equalsIgnoreCase(status)) {
	                    System.err.println("Payment already completed for this booking.");
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
	                System.out.println("\nPayment Successful! Your hall booking is confirmed.");
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
	        System.err.println("Payment Failed! Please try again.");
	        return false;
	    }
}