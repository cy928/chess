package dataAccess;

import request.AuthToken;

public interface AuthDAO {
    boolean checkAuthTokenInvalid(AuthToken authToken);

    AuthToken createAuthToken(AuthToken auth, String username);

    void deleteAuthToken(AuthToken authToken) throws DataAccessException;
    String getUsername(AuthToken authToken);
    void delete();
}
