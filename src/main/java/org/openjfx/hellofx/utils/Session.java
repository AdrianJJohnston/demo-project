package org.openjfx.hellofx.utils;

public class Session {
    private static volatile String loggedInUserEmail;
    private static volatile String userRole;

    public static synchronized void setLoggedInUser(String email) {
        loggedInUserEmail = email;
    }

    public static synchronized String getLoggedInUser() {
        return loggedInUserEmail;
    }

    public static synchronized void setUserRole(String role) {
        userRole = role;
    }

    public static synchronized String getUserRole() {
        return userRole;
    }

    public static synchronized boolean isUserLoggedIn() {
        return loggedInUserEmail != null;
    }

    public static synchronized void clearSession() {
        loggedInUserEmail = null;
        userRole = null;
    }
}