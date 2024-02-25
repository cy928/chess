package dataAccess;

import request.AuthToken;

public interface AuthDAO {
    AuthToken createAuthToken(String username);
    boolean checkAuthTokenInvalid(AuthToken authToken);
    void deleteAuthToken(AuthToken authToken) throws DataAccessException;
    String getUsername(AuthToken authToken);
    void delete();
}
