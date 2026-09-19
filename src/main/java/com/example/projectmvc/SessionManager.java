package com.example.projectmvc;

public class SessionManager {

    private static String username;

    public static void setUsername(String username) {
        SessionManager.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static void clearSession() {
        username = null;
    }
}
