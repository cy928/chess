package service;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class ClearService {
    public boolean delete() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        authDAO.delete();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        gameDAO.delete();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.delete();
        return true;
    }
}

