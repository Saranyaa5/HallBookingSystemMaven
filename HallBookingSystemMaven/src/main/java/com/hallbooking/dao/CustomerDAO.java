package com.hallbooking.dao;

import com.hallbooking.model.Customer;
import com.hallbooking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    
    public boolean registerCustomer(Customer customer) {
        String query = "INSERT INTO customer (user_id, name, email, pwd) VALUES (customer_id_sequence.NEXTVAL, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail()); // Corrected index
            pstmt.setString(3, customer.getPwd());   // Corrected index

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerByEmailAndPassword(String email, String pwd) {
        String query = "SELECT * FROM customer WHERE email = ? AND pwd = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, pwd);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("name"),
                        rs.getInt("user_id"), // Changed column name to match DB
                        rs.getString("email"),
                        rs.getString("pwd")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
