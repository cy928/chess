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
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGameDAO implements GameDAO {
    @Override
    public CreateGameResult createGame(CreateGameRequest information) throws DataAccessException {
        try (var connection=DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("INSERT INTO gameTable (gameName, chessGame) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, information.gameName());
                ChessBoard chessBoard = new ChessBoard();
                chessBoard.resetBoard();
                ChessGame chessGame = new ChessGame();
                chessGame.setBoard(chessBoard);
                preparedStatement.setString(2, new Gson().toJson(chessGame));
                preparedStatement.executeUpdate();
                ResultSet generatedKeys=preparedStatement.getGeneratedKeys();
                int gameID = 0;
                if (generatedKeys.next()) {
                    gameID = generatedKeys.getInt(1);
                }
                return new CreateGameResult(gameID);
            }
        } catch (SQLException exp) {
            throw new DataAccessException(exp.getMessage());
        }
    }
    @Override
    public void joinGame(String username, JoinGameRequest information) throws DataAccessException {
        try (var connection=DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("SELECT whiteUsername, blackUsername FROM gameTable WHERE gameID=?")) {
                preparedStatement.setInt(1, information.gameID());
                try (var rs=preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var whiteUsername=rs.getString("whiteUsername");
                        var blackUsername=rs.getString("blackUsername");
                        if (information.playerColor() == null) {
                            return;
                        }
                        if (information.playerColor().equals("WHITE")){
                            if (whiteUsername != null) {
                                throw new DataAccessException("Error: already taken");
                            }
                            else {
                                try (var preparedStatement2 = connection.prepareStatement("UPDATE gameTable SET whiteUsername=? WHERE gameID=?")) {
                                    preparedStatement2.setString(1, username);
                                    preparedStatement2.setInt(2, information.gameID());
                                    preparedStatement2.executeUpdate();
                                }
                            }
                        }
                        else {
                            if (blackUsername != null) {
                                throw new DataAccessException("Error: already taken");
                            }
                            else{
                                try (var preparedStatement3 = connection.prepareStatement("UPDATE gameTable SET blackUsername=? WHERE gameID=?")) {
                                    preparedStatement3.setString(1, username);
                                    preparedStatement3.setInt(2, information.gameID());
                                    preparedStatement3.executeUpdate();
                                }
                            }
                        }
                    }
                    else {
                        throw new DataAccessException( "Error: bad request");
                    }
                }
            }
        } catch (SQLException exp) {
            throw new DataAccessException(exp.getMessage());
        }
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
        } catch (SQLException exp) {
            throw new DataAccessException(exp.getMessage());
        }
    }

    @Override
    public void delete() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement=connection.prepareStatement("TRUNCATE gameTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exp) {
            throw new DataAccessException(exp.getMessage());
        }
    }
}
