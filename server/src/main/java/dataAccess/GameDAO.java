package dataAccess;

import chess.ChessGame;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

public interface GameDAO {
    CreateGameResult createGame(CreateGameRequest information) throws DataAccessException;
    void joinGame(String username, JoinGameRequest information) throws DataAccessException;
    ListGameResult getGameList() throws DataAccessException;

    ChessGame getSingleGame(Integer gameID) throws DataAccessException;

    void delete() throws DataAccessException ;

}
