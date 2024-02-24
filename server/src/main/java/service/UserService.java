package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;

public class UserService {
    public static AuthToken register(RegisterRequest information) throws DataAccessException {
        if (information.username() == null | information.password() == null | information.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        try {
            userDAO.createUser(information.username(), information.password(), information.email());
            AuthToken auth = authDAO.createAuth(information.username());
            return auth;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public static AuthToken login(LoginRequest information) throws DataAccessException {
        try {
            userDAO.checkCredential(information.username(), information.password());
            AuthToken auth = authDAO.createAuth(information.username());
            return auth;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public static boolean logout(AuthToken auth) throws DataAccessException {
        try {
            authDAO.logout(auth);
            return true;
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
