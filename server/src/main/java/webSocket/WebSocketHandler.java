package webSocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.AuthToken;
import serverMessages.Error;
import serverMessages.LoadGame;
import serverMessages.Notification;
import userGameCommand.*;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();
    SqlGameDAO gameDAO = new SqlGameDAO();
    SqlAuthDAO authDAO = new SqlAuthDAO();
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_OBSERVER -> joinObserver(session, msg);
            case JOIN_PLAYER -> joinPlayer(session, msg);
            case LEAVE -> leave(session, msg);
            case RESIGN -> resign(session, msg);
            case MAKE_MOVE -> makeMove(session, msg);
        }
    }

    private void joinObserver(Session session, String msg) throws IOException {
        JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);
        Connection conn = new Connection(command.getAuthString(), session);
        connectionManager.add(command.gameID, conn);
        try {
            if (authDAO.checkAuthTokenInvalid(new AuthToken(command.getAuthString()))) {
                throw new DataAccessException("Error: Invalid auth token.");
            }
            String userName = authDAO.getUsername(new AuthToken(command.getAuthString()));
            ChessGame game = gameDAO.getSingleGame(command.gameID);
            String message = String.format("%s has joined the game as an observer!", userName);
            Notification notification = new Notification(message);
            LoadGame loadGame = new LoadGame(game);
            connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
            connectionManager.sendToSelf(command.gameID, command.getAuthString(), loadGame);
        } catch (IOException | DataAccessException e) {
            Error errorMessage = new Error(e.getMessage());
            connectionManager.sendError(conn, errorMessage);
        }
    }

    private void joinPlayer(Session session, String msg) throws IOException, DataAccessException {
        JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
        Connection conn = new Connection(command.getAuthString(), session);
        connectionManager.add(command.gameID, conn);
        String playerColor;
        if (command.playerColor == null) {
            throw new DataAccessException(("Error: missing color"));
        } else if (command.playerColor == ChessGame.TeamColor.BLACK) {
            playerColor = "black";
        } else {
            playerColor = "white";
        }
        try {
            if (authDAO.checkAuthTokenInvalid(new AuthToken(command.getAuthString()))) {
                throw new DataAccessException("Error: Invalid auth token.");
            }
            String userName = authDAO.getUsername(new AuthToken(command.getAuthString()));
            gameDAO.checkGameID(command.gameID, command.playerColor, userName);
            ChessGame game = gameDAO.getSingleGame(command.gameID);
            String message = String.format("%s has joined the game as a %s player!", userName, playerColor);
            Notification notification = new Notification(message);
            LoadGame loadGame = new LoadGame(game);
            connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
            connectionManager.sendToSelf(command.gameID, command.getAuthString(), loadGame);
        } catch (IOException | DataAccessException ex) {
            Error errorMessage = new Error(ex.getMessage());
            connectionManager.sendError(conn, errorMessage);
        }
    }

    public void leave(Session session, String msg) throws IOException {
        Leave command = new Gson().fromJson(msg, Leave.class);
        Connection conn = new Connection(command.getAuthString(), session);
        try {
            if (authDAO.checkAuthTokenInvalid(new AuthToken(command.getAuthString()))) {
                throw new DataAccessException("Error: Invalid auth token.");
            }
            String userName=authDAO.getUsername(new AuthToken(command.getAuthString()));
            if (command.color != null) {
                gameDAO.checkGameID(command.gameID, command.color, userName);
                gameDAO.leavePlayer(command.gameID, command.color);
            }
            connectionManager.remove(command.gameID, conn);
            String message=String.format("%s has left the game!", userName);
            Notification notification=new Notification(message);
            connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
        } catch (IOException | DataAccessException ex) {
            Error errorMessage = new Error(ex.getMessage());
            connectionManager.sendError(conn, errorMessage);
        }
    }
    private void resign(Session session, String msg) throws IOException {
        Resign command = new Gson().fromJson(msg, Resign.class);
        Connection conn = new Connection(command.getAuthString(), session);
        try {
            if (authDAO.checkAuthTokenInvalid(new AuthToken(command.getAuthString()))) {
                throw new DataAccessException("Error: Invalid auth token.");
            }
            String userName=authDAO.getUsername(new AuthToken(command.getAuthString()));
            gameDAO.checkGameID(command.gameID, null, userName);
            gameDAO.getTeamColor(command.gameID, userName);
            gameDAO.deleteGameID(command.gameID);
            String message=String.format("%s has resigned from the game!", userName);
            Notification notification=new Notification(message);
            connectionManager.broadcast(command.gameID, null, notification);
            connectionManager.removeGameID(command.gameID);
        } catch (IOException | DataAccessException ex) {
            Error errorMessage = new Error(ex.getMessage());
            connectionManager.sendError(conn, errorMessage);
        }
    }

    private void makeMove(Session session, String msg) throws IOException {
        MakeMove command = new Gson().fromJson(msg, MakeMove.class);
        Connection conn = new Connection(command.getAuthString(), session);
        try {
            if (authDAO.checkAuthTokenInvalid(new AuthToken(command.getAuthString()))) {
                throw new DataAccessException("Error: Invalid auth token.");
            }
            String userName=authDAO.getUsername(new AuthToken(command.getAuthString()));
            gameDAO.checkGameID(command.gameID, null, userName);
            ChessGame game = gameDAO.getSingleGame(command.gameID);
            ChessGame.TeamColor teamColor = gameDAO.getTeamColor(command.gameID, userName);
            if (teamColor != game.getTeamTurn()) {
                throw new DataAccessException("Error: Invalid team turn.");
            }
            game.makeMove(command.move);
            gameDAO.updateGame(command.gameID, game);
            String message=String.format("%s has made a move!", userName);
            Notification notification=new Notification(message);
            connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
            connectionManager.sendGame(command.gameID, new LoadGame(game));
            String color;
            String opponent;
            if (teamColor == ChessGame.TeamColor.BLACK) {
                color = "Black";
                opponent = "White";
            } else {
                color = "White";
                opponent = "Black";
            }

            if (game.isInStalemate(teamColor)) {
                String result = "It's isInStalemate. It's a tie!";
                Notification notification1 = new Notification(result);
                connectionManager.broadcast(command.gameID, null, notification1);
                gameDAO.checkGameID(command.gameID, null, command.getAuthString());
                gameDAO.deleteGameID(command.gameID);
                connectionManager.removeGameID(command.gameID);
            }
            if (game.isInCheckmate(teamColor)) {

                String result = String.format("%sPlayer is isInCheckMate. %sPlayer win!",color, opponent );
                Notification notification2 = new Notification(result);
                connectionManager.broadcast(command.gameID, null, notification2);
                gameDAO.checkGameID(command.gameID, null, command.getAuthString());
                gameDAO.deleteGameID(command.gameID);
                connectionManager.removeGameID(command.gameID);
            }

        } catch (InvalidMoveException | DataAccessException ex) {
            Error errorMessage;
            if (ex.getMessage() != null) {
                errorMessage = new Error(ex.getMessage());
            } else {
                errorMessage = new Error("Error: This is an invalid move.");
            }
            connectionManager.sendError(conn, errorMessage);
        }
    }
}