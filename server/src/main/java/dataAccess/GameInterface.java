package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;

public interface GameInterface {
    public ListGameResponse getGameList(String username) throws DataAccessException;
    public void joinGame(String username, JoinGameRequest information) throws DataAccessException;

    public CreateGameResponse createGame(String username, CreateGameRequest information);

    public void delete() throws DataAccessException ;

}
