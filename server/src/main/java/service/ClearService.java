package service;
import dataAccess.*;
import dataAccessError.DataAccessException;

import java.sql.SQLException;

public class ClearService {
    public boolean delete() throws SQLException, DataAccessException {
        SqlAuthDAO authDAO = new SqlAuthDAO();
        authDAO.delete();
        SqlGameDAO gameDAO = new SqlGameDAO();
        gameDAO.delete();
        SqlUserDAO userDAO = new SqlUserDAO();
        userDAO.delete();
        return true;
    }
}

