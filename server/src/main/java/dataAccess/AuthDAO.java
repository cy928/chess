package dataAccess;

import request.AuthToken;

import java.sql.SQLException;

public interface AuthDAO {
    boolean checkAuthTokenInvalid(AuthToken authToken) throws DataAccessException, SQLException;

    void createAuthToken(AuthToken auth, String username) throws DataAccessException;

    void deleteAuthToken(AuthToken authToken) throws DataAccessException, SQLException;
    String getUsername(AuthToken authToken) throws DataAccessException;
    void delete() throws DataAccessException, SQLException;
}
