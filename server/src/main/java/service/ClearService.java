package service;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearService {
    public boolean delete() {
        AuthDAO authDAO = new AuthDAO();
        authDAO.delete();
        UserDAO userDAO = new UserDAO();
        userDAO.delete();
        GameDAO gameDAO = new GameDAO();
        gameDAO.delete();
        return true;
    }
}

