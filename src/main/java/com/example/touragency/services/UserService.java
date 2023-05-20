package com.example.touragency.services;

import com.example.touragency.daos.implementations.UserDao;
import com.example.touragency.entities.User;
import com.example.touragency.exceptions.ServiceException;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDao.getAllUsers();
        } catch (SQLException e) {
            throw new ServiceException("Failed to get all users", e);
        }
    }

    public User getUserById(long id) throws ServiceException {
        try {
            return userDao.getUserById(id);
        } catch (SQLException e) {
            throw new ServiceException("Failed to get user by ID: " + id, e);
        }
    }

    public void updateUser(User user) throws ServiceException {
        try {
            userDao.updateUser(user);
        } catch (SQLException e) {
            throw new ServiceException("Failed to update user: " + user, e);
        }
    }

    public void deleteUser(long id) throws ServiceException {
        try {
            userDao.deleteUser(id);
        } catch (SQLException e) {
            throw new ServiceException("Failed to delete user by ID: " + id, e);
        }
    }
}
