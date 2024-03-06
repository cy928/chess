package dataAccess;

import request.LoginRequest;
import request.RegisterRequest;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    @Override
    public void createUser(RegisterRequest information) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user(username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, information.username());
                preparedStatement.setString(2, information.password());
                preparedStatement.setString(3, information.email());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean checkCredential(LoginRequest information) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
                preparedStatement.setString(2, information.username());
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String password = result.getString("password");
                        return password.equals(information.password());
                    }
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public void delete() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("TRUNCATE user")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
