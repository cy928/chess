package dataAccess;

import request.AuthToken;

import java.util.*;

public class AuthDAO implements AuthInterface {
    static Map<AuthToken, String> authDB= new HashMap<>();
    @Override
    public void deleteAuth(AuthToken authToken) throws DataAccessException {
        if (authDB.containsKey(authToken)) {
            authDB.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
    @Override
    public String getUsername(AuthToken authToken) {
        return authDB.get(authToken);
    }

    @Override
    public boolean checkAuthToken(AuthToken authToken) {
      return !authDB.isEmpty() && authDB.get(authToken) != null;
    }

    @Override
    public AuthToken createAuthToken(String username) {
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        authDB.put(auth, username);
        return auth;
    }
    @Override
    public AuthToken checkAuthTokenExist(String username) {
        for (Map.Entry<AuthToken, String> entry : authDB.entrySet()) {
            if (Objects.equals(entry.getValue(), username)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void delete() {
        authDB.clear();
    }
}
