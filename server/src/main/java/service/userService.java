package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;

public class userService {
    public static AuthToken register(RegisterRequest information) throws DataAccessException {
        if (!userDAO.getUser(information.username())) {
            userDAO.creatUser(information.username(), information.passsword(), information.email());
            AuthToken auth = userDAO.creatAuth(information.username());
            return auth;
        }
        else {
            throw new DataAccessException("Error: already taken");
        }

    }
    public static AuthToken login(LoginRequest information) throws DataAccessException {
        AuthToken auth = userDAO.checkCredential(information.username(), information.password());
        return auth;
    }

}
