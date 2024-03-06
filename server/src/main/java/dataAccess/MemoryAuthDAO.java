package dataAccess;

import request.AuthToken;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO {
    static Map<AuthToken, String> authDB= new HashMap<>();
    @Override
    public AuthToken createAuthToken(AuthToken auth, String username) {
        authDB.put(auth, username);
        return auth;
    }
    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        if (!authDB.containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        } else {
            authDB.remove(authToken);
        }
    }
    @Override
    public boolean checkAuthTokenInvalid(AuthToken authToken) { return authDB.isEmpty() || authDB.get(authToken) == null; }
    @Override
    public String getUsername(AuthToken authToken) { return authDB.get(authToken); }
    @Override
    public void delete() { authDB.clear(); }
}
