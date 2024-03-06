package dataAccess;

import request.AuthToken;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO{

    @Override
    public void createAuthToken(AuthToken authToken, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth(authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, authToken.toString());
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean checkAuthTokenInvalid(AuthToken authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE authToken=?")) {
                preparedStatement.setString(1, authToken.toString());
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            return true;
        }
        return true;
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DELETE from auth WHERE authToken=?")) {
                preparedStatement.setString(1, authToken.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getUsername(AuthToken authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE authToken=?")) {
                preparedStatement.setString(1, authToken.toString());
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
    public void delete() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
