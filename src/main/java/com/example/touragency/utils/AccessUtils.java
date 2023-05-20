package com.example.touragency.utils;

import com.example.touragency.entities.UserType;

public class AccessUtils {
    public static boolean isUserAdmin(String userType) {
        return userType.equals(UserType.ADMIN.toString());
    }

}
