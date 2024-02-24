package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;



public class UserService {
    public AuthToken register(RegisterRequest information) throws DataAccessException {
        if (information.username() == null | information.password() == null | information.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.createUser(information);
            AuthDAO authDAO = new AuthDAO();
            AuthToken auth = authDAO.createAuth(information.username());
            return auth;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public AuthToken login(LoginRequest information) throws DataAccessException {
        UserDAO userDAO = new UserDAO();
        if (Boolean.FALSE.equals(userDAO.checkCredential(information))) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthDAO authDAO = new AuthDAO();
        AuthToken auth = authDAO.createAuth(information.username());
        return auth;
    }
    public Boolean logout(AuthToken auth) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        try {
            authDAO.deleteAuth(auth);
            return true;
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
