package service;

import dataAccess.*;
import request.AuthToken;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

public class GameService {
    public CreateGameResult createGame(AuthToken authToken, CreateGameRequest information) throws DataAccessException{
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
//        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        if (information.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        } else if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(authToken);
        return gameDAO.createGame(username, information);
    }
    public boolean joinGame(AuthToken authToken, JoinGameRequest information) throws DataAccessException{
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
//        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        MySqlGameDAO gameDAO = new MySqlGameDAO();
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
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();
//        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(authToken);
        return gameDAO.getGameList(username);
    }
}
