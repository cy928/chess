package dataAccess;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.LoginRequest;
import request.RegisterRequest;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    @Override
    public void createUser(RegisterRequest information) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(information.password());
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("INSERT INTO userTable (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, information.username());
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, information.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public boolean checkCredential(LoginRequest information) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM userTable WHERE username=?")) {
                preparedStatement.setString(1, information.username());
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String password = result.getString("password");
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        return encoder.matches(information.password(), password);
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
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("TRUNCATE userTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
