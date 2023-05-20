package com.example.touragency.sql;

public class UserQueries {
    public static final String GET_ALL_USERS = "SELECT * FROM users";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String UPDATE_USER = "UPDATE users SET email = ?, img = ?, name = ?, surname = ?, phone = ? WHERE id = ?;";
    public static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
}
