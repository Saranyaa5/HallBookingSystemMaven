package com.hallbooking.utils;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "HALLBOOKINGSYSTEM";
    private static final String PASSWORD = "saranya";

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); 
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver not found!", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}