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
    public Boolean joinGame(AuthToken auth, JoinGameRequest information) throws DataAccessException{
        if (information.gameID() == null) {
            throw new DataAccessException("Error: bad request");
        }
        AuthDAO dao = new AuthDAO();
        if (!dao.checkAuthToken(auth)) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        String username = authDAO.getUsername(auth);
        try {
            gameDAO.joinGame(username, information);
            return true;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public CreateGameResponse createGame(AuthToken auth, CreateGameRequest information) throws DataAccessException{
        if (information.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        if (!authDAO.checkAuthToken(auth)) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthDAO dao = new AuthDAO();
        String username = dao.getUsername(auth);
        CreateGameResponse resp = gameDAO.createGame(information);
        return resp;
    }
}
