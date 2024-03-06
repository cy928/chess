package dataAccess;

import request.AuthToken;

public class MySqlAuthDAO implements AuthDAO{

    @Override
    public AuthToken createAuthToken(AuthToken auth, String username) {
        return null;
    }

    @Override
    public boolean checkAuthTokenInvalid(AuthToken authToken) {
        return false;
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {

    }

    @Override
    public String getUsername(AuthToken authToken) {
        return null;
    }

    @Override
    public void delete() {

    }
}
