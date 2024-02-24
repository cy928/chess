package dataAccess;

import request.AuthToken;

import java.util.Dictionary;
import java.util.UUID;

public class AuthDAO implements AuthInterface {
    static Dictionary<AuthToken, String> userDB;
    @Override
    public void deleteAuth(AuthToken authToken) throws DataAccessException {
        if(!userDB.isEmpty() && userDB.get(authToken) != null) {
            userDB.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
    @Override
    public String getUsername(AuthToken authToken) {
        return userDB.get(authToken);
    }

    @Override
    public boolean checkAuthToken(AuthToken authToken) {
        if (!userDB.isEmpty() & userDB.get(authToken) != null) {
            return true;
        }
        return false;
    }

    @Override
    public AuthToken createAuth(String username) {
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        userDB.put(auth, username);
        return auth;
    }
}
