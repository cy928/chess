package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGameDAO implements GameDAO {
    @Override
    public CreateGameResult createGame(CreateGameRequest information) throws DataAccessException {
        try (Connection connection=DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO gameTable (gameName, chessGame) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
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
        try (Connection connection=DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement=connection.prepareStatement("SELECT whiteUsername, blackUsername FROM gameTable WHERE gameID=?")) {
                preparedStatement.setInt(1, information.gameID());
                try (ResultSet result=preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String blackUsername=result.getString("blackUsername");
                        String whiteUsername=result.getString("whiteUsername");
                        if (information.playerColor() == null) {
                            return; // observe
                        }
                        if (information.playerColor().equalsIgnoreCase("BLACK")) {
                            if (blackUsername != null) {
                                throw new DataAccessException("Error: already taken");
                            }
                            else {
                                try (PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE gameTable SET blackUsername=? WHERE gameID=?")) {
                                    preparedStatement3.setString(1, username);
                                    preparedStatement3.setInt(2, information.gameID());
                                    preparedStatement3.executeUpdate();
                                }
                            }
                        }
                        else {
                            if (whiteUsername != null) {
                                throw new DataAccessException("Error: already taken");
                            }
                            else {
                                try (PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE gameTable SET whiteUsername=? WHERE gameID=?")) {
                                    preparedStatement2.setString(1, username);
                                    preparedStatement2.setInt(2, information.gameID());
                                    preparedStatement2.executeUpdate();
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
        try (Connection connection=DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM  gameTable ")) {
                try (ResultSet result=preparedStatement.executeQuery()) {
                    while (result.next()) {
                        Integer id = result.getInt("gameID");
                        String blackUsername = result.getString("blackUsername");
                        String whiteUsername = result.getString("whiteUsername");
                        String gameName=result.getString("gameName");
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
    public ChessGame getSingleGame(Integer gameID) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        try (Connection connection = DatabaseManager.getConnection())  {
            try (PreparedStatement prepStatement = connection.prepareStatement("SELECT id FROM gameTable WHERE gameID=? ")) {
                try (ResultSet result = prepStatement.executeQuery()) {
                    String game = "";
                    if (result.next()) {
                        game = result.getString("id");
                    }
                    ChessBoard chessBoard = new Gson().fromJson(game, ChessBoard.class);
                    chessGame.setBoard(chessBoard);
                    return chessGame;
               }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void delete() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement=connection.prepareStatement("TRUNCATE gameTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exp) {
            throw new DataAccessException(exp.getMessage());
        }
    }
}
