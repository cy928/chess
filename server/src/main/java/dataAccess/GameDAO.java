package dataAccess;

import chess.ChessGame;
import dataAccessError.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

public interface GameDAO {
    CreateGameResult createGame(CreateGameRequest information) throws DataAccessException;
    void joinGame(String username, JoinGameRequest information) throws DataAccessException;
    ListGameResult getGameList() throws DataAccessException;

    ChessGame getSingleGame(Integer gameID) throws DataAccessException;
    void leavePlayer(Integer gameID, ChessGame.TeamColor teamColor)throws DataAccessException;
    void checkGameID(Integer gameID, ChessGame.TeamColor teamColor, String username) throws DataAccessException;
    void deleteGameID(Integer gameID) throws DataAccessException;
    ChessGame.TeamColor getTeamColor(Integer gameID, String userName) throws DataAccessException;
    void updateGame(Integer gameID, ChessGame chessGame) throws DataAccessException;
    void delete() throws DataAccessException ;
}
