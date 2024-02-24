package dataAccess;

import request.AuthToken;
import request.LoginRequest;
import response.ListGameResponse;

public interface AuthInterface {
    public void deleteAuth(AuthToken authToken) throws DataAccessException;

    public String getUsername(AuthToken authToken);

    public boolean checkAuthToken(AuthToken authToken);

    public AuthToken createAuth(String username);
}
