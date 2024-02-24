package dataAccess;

import request.AuthToken;
import request.LoginRequest;
import response.ListGameResponse;

public interface AuthInterface {
    public void logout(AuthToken authToken) throws DataAccessException;
    public String getUsername(AuthToken authToken) throws DataAccessException;
    public void checkAuthToken(AuthToken authToken) throws DataAccessException;
    public AuthToken createAuth(String username) throws DataAccessException;
}
