package service;

import dataAccess.DataAccessException;
import request.AuthToken;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;



public class GameService {
    public static ListGameResponse getGameList(AuthToken auth) throws DataAccessException{
        String username = authDAO.getUsername(auth);
        try {
            authDAO.checkAuthToken(auth);
            ListGameResponse gameList = gameDAO.getGameList(username);
            return gameList;

        } catch(DataAccessException e) {
            throw e;
        }
    }
    public static boolean joinGame(AuthToken auth, JoinGameRequest information) throws DataAccessException{
        if (information.gameID() == null) {
            throw new DataAccessException("Error: bad request");
        }
        try {
            authDAO.checkAuthToken(auth);
        } catch(DataAccessException e) {
            throw e;
        }
        String username = authDAO.getUsername(auth);
        try {
            gameDAO.joinGame(username, information);
        } catch(DataAccessException e) {
            throw e;
        }
        return true;
    }
    public static CreateGameResponse createGame(AuthToken auth, CreateGameRequest information) throws DataAccessException{
        if (information.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }
        try {
            authDAO.checkAuthToken(auth);
        } catch(DataAccessException e) {
            throw e;
        }
        String username = authDAO.getUsername(auth);
        CreateGameResponse resp = gameDAO.createGame(username, information);
        return resp;
    }
}
