package service;

import dataAccess.*;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;

import java.sql.SQLException;
import java.util.UUID;


public class UserService {
    public AuthToken login(LoginRequest information) throws DataAccessException {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
//        MemoryUserDAO userDAO = new MemoryUserDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        MySqlUserDAO userDAO = new MySqlUserDAO();
        if (Boolean.FALSE.equals(userDAO.checkCredential(information))) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(auth, information.username());
        return auth;
    }
    public boolean logout(AuthToken authToken) throws DataAccessException, SQLException {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        authDAO.deleteAuthToken(authToken);
        return true;
    }
    public AuthToken register(RegisterRequest information) throws DataAccessException {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
//        MemoryUserDAO userDAO = new MemoryUserDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        MySqlUserDAO userDAO = new MySqlUserDAO();
        if (information.username() == null || information.password() == null || information.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        userDAO.createUser(information);
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(auth, information.username());
        return auth;
    }
}
