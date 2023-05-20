package com.example.touragency.daos.interfaces;

import com.example.touragency.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDaoInterface {
    User register(User user) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    User getUserById(long id) throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUser(long id) throws SQLException;

    User getUserByEmail(String email) throws SQLException;
}
