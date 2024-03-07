package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

public interface GameDAO {
    CreateGameResult createGame(String username, CreateGameRequest information) throws DataAccessException;
    void joinGame(String username, JoinGameRequest information) throws DataAccessException;
    ListGameResult getGameList() throws DataAccessException;
    void delete() throws DataAccessException ;

}
