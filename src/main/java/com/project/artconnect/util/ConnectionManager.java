package com.project.artconnect.util;

import com.project.artconnect.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage JDBC connections.
 */
public class ConnectionManager {
    private static String currentUser = DatabaseConfig.READ_USER;
    private static String currentPassword = DatabaseConfig.READ_PASSWORD;

    /**
     * Provides a connection to the MySQL database.
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConfig.URL,
            currentUser,
            currentPassword
        );
    }

    public static void setCredentials(String user, String password) {
        currentUser = user;
        currentPassword = password;
    }

    public static void resetToDefaultConnection() {
        currentUser = DatabaseConfig.READ_USER;
        currentPassword = DatabaseConfig.READ_PASSWORD;
    }
}
