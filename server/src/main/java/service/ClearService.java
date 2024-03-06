package service;
import dataAccess.*;

import java.sql.SQLException;

public class ClearService {
    public boolean delete() throws SQLException, DataAccessException {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        authDAO.delete();
//        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        gameDAO.delete();
//        MemoryUserDAO userDAO = new MemoryUserDAO();
        MySqlUserDAO userDAO = new MySqlUserDAO();
        userDAO.delete();
        return true;
    }
}

