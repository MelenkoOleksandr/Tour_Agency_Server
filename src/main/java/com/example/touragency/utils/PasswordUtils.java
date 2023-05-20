package com.example.touragency.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    private static final int WORKLOAD = 12;

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(password, salt);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(password, hashedPassword);
    }
}
