package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import response.ListGameResponse;

public class ClearService {
    public static boolean deleteall() throws DataAccessException {
        gameDAO.deleteall();
        return true;
    }
}
