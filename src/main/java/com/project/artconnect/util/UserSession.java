package com.project.artconnect.util;

import com.project.artconnect.config.DatabaseConfig;

public class UserSession {
    public enum Role {
        NONE,
        READ_ONLY,
        ADMIN
    }

    private static String username = DatabaseConfig.READ_USER;
    private static Role role = Role.NONE;

    public static void loginAdmin() {
        username = DatabaseConfig.ROOT_USER;
        role = Role.ADMIN;
    }

    public static void loginReadOnly() {
        username = DatabaseConfig.READ_USER;
        role = Role.READ_ONLY;
    }

    public static void logout() {
        username = DatabaseConfig.READ_USER;
        role = Role.READ_ONLY;
    }

    public static boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public static boolean isReadOnly() {
        return role == Role.READ_ONLY;
    }

    public static boolean isLoggedIn() {
        return role != Role.NONE;
    }

    public static String getCurrentUsername() {
        return username;
    }

    public static String getDisplayName() {
        if (isAdmin()) {
            return username + " (admin)";
        }
        if (isReadOnly()) {
            return username + " (read-only)";
        }
        return username + " (guest)";
    }
}
