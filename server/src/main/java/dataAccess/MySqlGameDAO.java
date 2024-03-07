package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MySqlGameDAO implements GameDAO {
    @Override
    public CreateGameResult createGame(String username, CreateGameRequest information) throws DataAccessException {
        try (var connection=DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("INSERT INTO gameTable (gameName, chessGame) VALUES(?, ?)")) {
                preparedStatement.setString(1, information.gameName());
                ChessBoard chessBoard = new ChessBoard();
                chessBoard.resetBoard();
                ChessGame chessGame = new ChessGame();
                chessGame.setBoard(chessBoard);
                preparedStatement.setString(2, new Gson().toJson(chessGame));
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys=preparedStatement.getGeneratedKeys()) {
                    int gameID = 0;
                    if (generatedKeys.next()) {
                        gameID = generatedKeys.getInt(1);
                    }
                    return new CreateGameResult(gameID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void joinGame(String username, JoinGameRequest information) throws DataAccessException {

    }

    @Override
    public ListGameResult getGameList() throws DataAccessException {
        List<Game> gameList = new ArrayList<>();
        try (var connection=DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("SELECT * FROM  gameTable ")) {
                try (var result=preparedStatement.executeQuery()) {
                    while (result.next()) {
                        var id=result.getInt("gameID");
                        var whiteUsername=result.getString("whiteUsername");
                        var blackUsername=result.getString("blackUsername");
                        var gameName=result.getString("gameName");
                        gameList.add(new Game(id, whiteUsername, blackUsername, gameName));
                    }
                    return new ListGameResult(gameList);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void delete() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("TRUNCATE gameTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
