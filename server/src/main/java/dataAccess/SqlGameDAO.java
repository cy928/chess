package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccessError.DataAccessException;
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
                chessGame.setTeamTurn(ChessGame.TeamColor.WHITE);
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
        try (Connection connection = DatabaseManager.getConnection())  {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT chessGame FROM gameTable WHERE gameID=? ")) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    String chessGame = "";
                    if (result.next()) {
                        chessGame = result.getString("chessGame");
                    } else {
                        throw new DataAccessException("Error: Did not find any chess game.");
                    }
                    return new Gson().fromJson(chessGame, ChessGame.class);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void leavePlayer(Integer gameID, ChessGame.TeamColor teamColor) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            if (teamColor.toString().equalsIgnoreCase("black")) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE gameTable SET blackUsername=? WHERE gameID=?")) {
                    preparedStatement.setString(1, null);
                    preparedStatement.setInt(2, gameID);
                    preparedStatement.executeUpdate();
                }
            } else if (teamColor.toString().equalsIgnoreCase("white")) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE gameTable SET whiteUsername=? WHERE gameID=?")) {
                    preparedStatement.setString(1, null);
                    preparedStatement.setInt(2, gameID);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public ChessGame.TeamColor getTeamColor(Integer gameID, String userName) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT whiteUsername,blackUsername FROM gameTable WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String blackUsername = result.getString("blackUsername");
                        String whiteUsername = result.getString("whiteUsername");
                        if (blackUsername.equalsIgnoreCase(userName)){
                            return ChessGame.TeamColor.BLACK;
                        } else if (whiteUsername.equalsIgnoreCase(userName)){
                            return ChessGame.TeamColor.WHITE;
                        } else {
                            throw new DataAccessException("Error: Observer ia not able to resign.");
                        }
                    } else {
                        throw new DataAccessException("Error: Did not find any chess game.");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void updateGame(Integer gameID, ChessGame chessGame) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE gameTable SET chessGame=? WHERE gameID=?")) {
                preparedStatement.setString(1, new Gson().toJson(chessGame));
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void checkGameID(Integer gameID, ChessGame.TeamColor teamColor, String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            if (teamColor == null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM gameTable WHERE gameID=?")) {
                    preparedStatement.setInt(1, gameID);
                    try (ResultSet result = preparedStatement.executeQuery()) {
                        if (result.next()) {
                            return;
                        } else {
                            throw new DataAccessException("Error: Did not find any chess game");
                        }
                    }
                }
            } else if (teamColor.toString().equalsIgnoreCase("black")) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT blackUsername FROM gameTable WHERE gameID=?")) {
                    preparedStatement.setInt(1, gameID);
                    try (ResultSet result = preparedStatement.executeQuery()) {
                        if (result.next()) {
                            String blackUsername = result.getString("blackUsername");
                            if (!username.equalsIgnoreCase(blackUsername)) {
                                throw new DataAccessException("Error: Usernames do not match.");
                            }
                        } else {
                            throw new DataAccessException("Error: Did not find any chess game");
                        }
                    }
                }
            } else if (teamColor.toString().equalsIgnoreCase("white")) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT whiteUsername FROM gameTable WHERE gameID=?")) {
                    preparedStatement.setInt(1, gameID);
                    try (ResultSet result = preparedStatement.executeQuery()) {
                        if (result.next()) {
                            String whiteUsername = result.getString("whiteUsername");
                            if (!username.equalsIgnoreCase(whiteUsername)) {
                                throw new DataAccessException("Error: Usernames do not match.");
                            }
                        } else {
                            throw new DataAccessException("Error: Did not find any chess game");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    @Override
    public void deleteGameID(Integer gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM gameTable WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
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
