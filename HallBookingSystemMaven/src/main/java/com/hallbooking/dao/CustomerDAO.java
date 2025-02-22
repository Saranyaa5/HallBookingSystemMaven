package com.hallbooking.dao;

import com.hallbooking.model.Customer;
import com.hallbooking.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    public boolean registerCustomer(Customer customer) {
        String sql = "INSERT INTO customer (user_id, name, email, pwd) VALUES (customer_id_sequence.NEXTVAL, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPassword());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email = ? AND pwd = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                    resultSet.getString("name"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("email"),
                    resultSet.getString("pwd")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}