package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;

import java.util.UUID;


public class UserService {
    public AuthToken login(LoginRequest information) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        if (Boolean.FALSE.equals(userDAO.checkCredential(information))) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        return authDAO.createAuthToken(auth, information.username());
    }
    public boolean logout(AuthToken authToken) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        authDAO.deleteAuthToken(authToken);
        return true;
    }
    public AuthToken register(RegisterRequest information) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        if (information.username() == null || information.password() == null || information.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        userDAO.createUser(information);
        AuthToken auth = new AuthToken(UUID.randomUUID().toString());
        return authDAO.createAuthToken(auth, information.username());
    }
}
