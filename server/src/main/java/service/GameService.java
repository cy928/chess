package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;

public class GameService {
    public CreateGameResult createGame(AuthToken authToken, CreateGameRequest information) throws DataAccessException{
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        if (information.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        } else if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(authToken);
        return gameDAO.createGame(username, information);
    }
    public boolean joinGame(AuthToken authToken, JoinGameRequest information) throws DataAccessException{
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
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
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        if (authDAO.checkAuthTokenInvalid(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(authToken);
        return gameDAO.getGameList(username);
    }
}
