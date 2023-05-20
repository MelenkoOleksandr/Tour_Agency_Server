package com.example.touragency.daos.implementations;

import com.example.touragency.connection.PostgresConnectionPool;
import com.example.touragency.daos.interfaces.UserDaoInterface;
import com.example.touragency.entities.User;
import com.example.touragency.entities.UserType;
import com.example.touragency.sql.AuthQueries;
import com.example.touragency.sql.UserQueries;

import com.example.touragency.utils.PasswordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements UserDaoInterface {
    private static final Logger logger = LogManager.getLogger(UserDao.class);
    @Override
    public User register(User user) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            User existingUser = getUserByEmail(user.getEmail());

            if (existingUser != null) {
                logger.error("User with email " + user.getEmail() + " already exists");
                throw new SQLException("User with email " + user.getEmail() + " already exists");
            }

            PreparedStatement statement = connection.prepareStatement(AuthQueries.REGISTER);
            statement.setString(1, user.getEmail());
            statement.setString(2, PasswordUtils.hashPassword(user.getPassword()));
            statement.setString(3, user.getUserType().getName());
            statement.setString(4, user.getName());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("User registered successfully");
                return getUserByEmail(user.getEmail());
            } else {
                logger.error("Error registering user");
                throw new SQLException("Error registering user");
            }
        } catch (SQLException ex) {
            logger.error("Error registering user: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UserQueries.GET_ALL_USERS);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = parseUserFromDB(rs);
                users.add(user);
            }
        } catch (SQLException ex) {
            logger.error("Error retrieving all users: " + ex.getMessage());
            throw ex;
        }
        return users;
    }

    public User getUserById(long id) throws SQLException {
        User user = null;
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UserQueries.GET_USER_BY_ID);
            statement.setInt(1, (int) id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = parseUserFromDB(rs);
            }
        } catch (SQLException ex) {
            logger.error("Error retrieving user by id: " + ex.getMessage());
            throw ex;
        }
        return user;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UserQueries.UPDATE_USER);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getImg());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getPhone());
            statement.setInt(6, Math.toIntExact(user.getId()));
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("User updated successfully: " + user);
            }
        } catch (SQLException ex) {
            logger.error("Error updating user: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteUser(long id) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UserQueries.DELETE_USER);
            statement.setInt(1, (int) id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("User deleted successfully with id: " + id);
            }
        } catch (SQLException ex) {
            logger.error("Error deleting user: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        User user = null;
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UserQueries.GET_USER_BY_EMAIL);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = parseUserFromDB(rs);
            }
        } catch (SQLException ex) {
            logger.error("Error retrieving user by email: " + ex.getMessage());
            throw ex;
        }
        return user;
    }

    public User parseUserFromDB(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("img"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("phone"),
                UserType.getUserTypeByName(rs.getString("user_type"))
        );
    }
}
