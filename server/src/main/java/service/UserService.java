package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;



public class UserService {
    public AuthToken login(LoginRequest information) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        if (Boolean.FALSE.equals(userDAO.checkCredential(information))) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken auth = authDAO.createAuthToken(information.username());
        return auth;
    }
    public void logout(AuthToken auth) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        try {
            authDAO.deleteAuth(auth);
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public AuthToken register(RegisterRequest information) throws DataAccessException {
        if (information.username() == null || information.password() == null || information.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.createUser(information);
            AuthToken auth = authDAO.createAuthToken(information.username());
            return auth;
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
