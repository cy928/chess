package service;
import dataAccess.GameDAO;

public class ClearService {
    public void delete() {
        GameDAO gameDAO = new GameDAO();
        gameDAO.delete();
    }
}

