package service;

import dataAccess.*;
import dataAccessError.DataAccessException;
import request.AuthToken;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

public class GameService {
    public CreateGameResult createGame(AuthToken authToken, CreateGameRequest information) throws DataAccessException {
        SqlAuthDAO authDAO = new SqlAuthDAO();
        SqlGameDAO gameDAO = new SqlGameDAO();
        if (information.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        } else if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.createGame(information);
    }
    public boolean joinGame(AuthToken authToken, JoinGameRequest information) throws DataAccessException{
        SqlAuthDAO authDAO = new SqlAuthDAO();
        SqlGameDAO gameDAO = new SqlGameDAO();
        if (information.gameID() == null) {
            throw new DataAccessException("Error: bad request");
        } else if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(authToken);
        gameDAO.joinGame(username, information);
        return true;
    }
    public ListGameResult getGameList(AuthToken authToken) throws DataAccessException{
        SqlAuthDAO authDAO = new SqlAuthDAO();
        SqlGameDAO gameDAO = new SqlGameDAO();
        if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.getGameList();
    }
}
