// src/main/java/com/example/dao/DatabaseConnection.java
package com.example.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    // Don't keep a static connection - create new one for each request
    public static Connection getConnection() {
        try {
            Properties prop = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }

            prop.load(input);

            // Check environment variables first (for production)
            String url = System.getenv("DB_URL") != null ?
                    System.getenv("DB_URL") : prop.getProperty("db.url");
            String username = System.getenv("DB_USERNAME") != null ?
                    System.getenv("DB_USERNAME") : prop.getProperty("db.username");
            String password = System.getenv("DB_PASSWORD") != null ?
                    System.getenv("DB_PASSWORD") : prop.getProperty("db.password");

            // Validate that we have connection details
            if (url == null || username == null || password == null) {
                throw new RuntimeException("Database connection details are missing. " +
                        "Please check database.properties or set environment variables.");
            }

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    // Helper method to close connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}