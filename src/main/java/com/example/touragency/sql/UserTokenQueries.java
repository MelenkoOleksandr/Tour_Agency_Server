package com.example.touragency.sql;

public class UserTokenQueries {
    public static final String GET_USER_TOKENS_BY_USER_ID = "SELECT * FROM user_tokens WHERE user_id = ?";
    public static final String UPDATE_ACCESS_TOKEN = "UPDATE user_tokens SET access_token = ? WHERE user_id = ?";
    public static final String UPDATE_REFRESH_TOKEN = "UPDATE user_tokens SET refresh_token = ? WHERE user_id = ?";
    public static final String SAVE_USER_TOKEN = "INSERT INTO user_tokens(user_id, access_token, refresh_token) VALUES (?, ?, ?)";
    public static final String DELETE_USER_TOKENS_BY_USER_ID = "DELETE FROM user_tokens WHERE user_id = ?";
}
