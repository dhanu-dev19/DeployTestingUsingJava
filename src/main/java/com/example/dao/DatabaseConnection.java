// src/main/java/com/example/dao/DatabaseConnection.java
package com.example.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    static {
        // Load PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ PostgreSQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL JDBC Driver not found!");
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() {
        System.out.println("\n🔍 Attempting database connection...");

        try {
            // FIRST PRIORITY: DATABASE_URL environment variable (Render standard)
            String databaseUrl = System.getenv("DATABASE_URL");

            if (databaseUrl != null && !databaseUrl.trim().isEmpty()) {
                System.out.println("✓ Using DATABASE_URL from environment");
                // Convert postgresql:// to jdbc:postgresql://
                databaseUrl = convertToJDBCUrl(databaseUrl);
                System.out.println("Connecting with DATABASE_URL (masked): " + maskPassword(databaseUrl));
                return DriverManager.getConnection(databaseUrl);
            }

            // SECOND PRIORITY: Individual environment variables
            String dbUrl = System.getenv("DB_URL");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            if (dbUrl != null && username != null && password != null) {
                System.out.println("✓ Using DB_URL, DB_USERNAME, DB_PASSWORD from environment");
                System.out.println("Connecting to: " + dbUrl);
                System.out.println("Username: " + username);
                return DriverManager.getConnection(dbUrl, username, password);
            }

            // THIRD PRIORITY: database.properties file (local development)
            Properties prop = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input != null) {
                System.out.println("✓ Using database.properties file");
                prop.load(input);
                input.close();

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.username");
                String pass = prop.getProperty("db.password");

                if (url != null && user != null && pass != null) {
                    System.out.println("Connecting to: " + url);
                    System.out.println("Username: " + user);
                    return DriverManager.getConnection(url, user, pass);
                }
            } else {
                System.out.println("⚠️ database.properties not found in classpath");
            }

            // Print debug information
            printDebugInfo();

            throw new RuntimeException("No database configuration found. " +
                    "Please set DATABASE_URL or DB_URL/DB_USERNAME/DB_PASSWORD " +
                    "environment variables, or create database.properties file.");

        } catch (Exception e) {
            System.err.println("❌ Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    private static String convertToJDBCUrl(String url) {
        // Convert postgresql:// to jdbc:postgresql://
        if (url.startsWith("postgresql://")) {
            return "jdbc:" + url;
        } else if (url.startsWith("postgres://")) {
            return "jdbc:postgresql://" + url.substring(11);
        } else if (!url.startsWith("jdbc:postgresql://")) {
            // Assume it's not a JDBC URL, try to convert
            return "jdbc:postgresql://" + url;
        }
        return url; // Already jdbc:postgresql://
    }

    private static String maskPassword(String url) {
        // Mask password in logs for security
        try {
            if (url.contains("@")) {
                String[] parts = url.split("@");
                String credentials = parts[0].replace("jdbc:postgresql://", "");
                if (credentials.contains(":")) {
                    String[] credParts = credentials.split(":");
                    if (credParts.length >= 2) {
                        // Mask the password
                        return url.replace(credParts[1], "*****");
                    }
                }
            }
        } catch (Exception e) {
            // If masking fails, return original
        }
        return url;
    }

    private static void printDebugInfo() {
        System.out.println("\n=== DATABASE CONFIGURATION DEBUG ===");

        // Check DATABASE_URL
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null) {
            System.out.println("DATABASE_URL: [SET]");
            System.out.println("  Length: " + dbUrl.length() + " chars");
            System.out.println("  Starts with: " +
                    (dbUrl.length() > 20 ? dbUrl.substring(0, 20) + "..." : dbUrl));
        } else {
            System.out.println("DATABASE_URL: [NOT SET]");
        }

        // Check DB_URL
        String dbUrl2 = System.getenv("DB_URL");
        System.out.println("DB_URL: " + (dbUrl2 != null ? "[SET]" : "[NOT SET]"));

        // Check DB_USERNAME
        String dbUser = System.getenv("DB_USERNAME");
        System.out.println("DB_USERNAME: " + (dbUser != null ? "[SET]" : "[NOT SET]"));

        // Check DB_PASSWORD
        String dbPass = System.getenv("DB_PASSWORD");
        System.out.println("DB_PASSWORD: " + (dbPass != null ? "[SET]" : "[NOT SET]"));

        // Check for database.properties
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            System.out.println("database.properties: " +
                    (input != null ? "[FOUND]" : "[NOT FOUND]"));
        } catch (Exception e) {
            System.out.println("database.properties: [ERROR CHECKING]");
        }

        System.out.println("====================================\n");
    }

    // Helper method to close connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}