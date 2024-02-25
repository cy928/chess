package dataAccess;

import request.AuthToken;

public interface AuthInterface {
    public void deleteAuth(AuthToken authToken) throws DataAccessException;

    public String getUsername(AuthToken authToken);

    public boolean checkAuthToken(AuthToken authToken);

    public AuthToken createAuthToken(String username);

  AuthToken checkAuthTokenExist(String username);

  void delete();
}
