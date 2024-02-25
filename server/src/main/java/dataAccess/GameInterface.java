package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;

public interface GameInterface {
    CreateGameResponse createGame(String username, CreateGameRequest information);
    void joinGame(String username, JoinGameRequest information) throws DataAccessException;
    ListGameResponse getGameList(String username) throws DataAccessException;
    void delete() throws DataAccessException ;

}
