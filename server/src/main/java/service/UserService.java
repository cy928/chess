package service;

import dataAccess.*;
import dataAccessError.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;

import java.sql.SQLException;
import java.util.UUID;


public class UserService {
    public AuthToken login(LoginRequest information) throws DataAccessException {
        SqlAuthDAO authDAO = new SqlAuthDAO();
        SqlUserDAO userDAO = new SqlUserDAO();
        if (Boolean.FALSE.equals(userDAO.checkCredential(information))) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(auth, information.username());
        return auth;
    }
    public boolean logout(AuthToken authToken) throws DataAccessException, SQLException {
        SqlAuthDAO authDAO = new SqlAuthDAO();
        if (authDAO.checkAuthTokenInvalid(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuthToken(authToken);
        return true;
    }
    public AuthToken register(RegisterRequest information) throws DataAccessException {
        SqlAuthDAO authDAO = new SqlAuthDAO();
        SqlUserDAO userDAO = new SqlUserDAO();
        if (information.username() == null || information.password() == null || information.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        userDAO.createUser(information);
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(auth, information.username());
        return auth;
    }
}
