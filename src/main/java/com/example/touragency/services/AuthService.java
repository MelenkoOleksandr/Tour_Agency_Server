package com.example.touragency.services;

import com.example.touragency.daos.implementations.UserDao;
import com.example.touragency.entities.User;
import com.example.touragency.exceptions.AuthException;
import com.example.touragency.exceptions.ServiceException;
import com.example.touragency.utils.PasswordUtils;
import com.example.touragency.utils.TokenCreator;


import java.sql.SQLException;

public class AuthService {
    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User login(String email, String password) throws AuthException, SQLException {
        User user = userDao.getUserByEmail(email);
        if (user == null || !PasswordUtils.checkPassword(password, user.getPassword())) {
            throw new AuthException("Invalid email or password");
        }

        return user;
    }

    public User register(User user) throws ServiceException {
        try {
            return userDao.register(user);
        } catch (SQLException e) {
            throw new ServiceException("Failed to register user: " + user, e);
        }
    }
}