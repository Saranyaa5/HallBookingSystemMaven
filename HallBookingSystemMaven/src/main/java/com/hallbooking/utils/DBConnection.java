package com.hallbooking.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Update with your database URL
    private static final String USERNAME = "your_username"; // Update with your database username
    private static final String PASSWORD = "your_password"; // Update with your database password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}