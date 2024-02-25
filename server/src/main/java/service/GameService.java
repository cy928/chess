package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;

public class GameService {
    public CreateGameResponse createGame(AuthToken auth, CreateGameRequest information) throws DataAccessException{
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        if (information.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (!authDAO.checkAuthToken(auth)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(auth);
        CreateGameResponse resp = gameDAO.createGame(username, information);
        return resp;
    }
    public void joinGame(AuthToken auth, JoinGameRequest information) throws DataAccessException{
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        if (information.gameID() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (!authDAO.checkAuthToken(auth)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(auth);
        try {
            gameDAO.joinGame(username, information);
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public ListGameResponse getGameList(AuthToken auth) throws DataAccessException{
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        if (!authDAO.checkAuthToken(auth)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = authDAO.getUsername(auth);
        ListGameResponse gameList = gameDAO.getGameList(username);
        return gameList;
    }
}
