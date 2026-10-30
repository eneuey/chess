package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.*;

public class SQLUserDAO {
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE userData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
    public void createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        String email = userData.email();
        String password = userData.password();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement
                     ("INSERT INTO userData (username, email, password) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create user", ex);
        }
    }
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement
                     ("SELECT username, email, password FROM userData WHERE username=?")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String retrievedUsername = resultSet.getString(1);
                    String retrivedEmail = resultSet.getString(2);;
                    String retrievedPassword = resultSet.getString(3);
                    return new UserData(retrievedUsername, retrivedEmail, retrievedPassword);
                }
                else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get user", ex);
        }
    }
}
