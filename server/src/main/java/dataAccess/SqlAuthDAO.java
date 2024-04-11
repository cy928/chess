package dataAccess;

import dataAccessError.DataAccessException;
import request.AuthToken;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO{

    @Override
    public void createAuthToken(AuthToken authToken, String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("INSERT INTO authTable (authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, authToken.authToken());
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean checkAuthTokenInvalid(AuthToken authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken.authToken());
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("DELETE FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken.authToken());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
                throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public String getUsername(AuthToken authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT username FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken.authToken());
                try (var result=preparedStatement.executeQuery()) {
                    if (result.next()) {
                        return result.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void delete() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("TRUNCATE authTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
