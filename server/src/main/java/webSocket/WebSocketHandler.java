package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
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
            String userName = authDAO.getUsername(new AuthToken(command.getAuthString()));
            ChessGame game = gameDAO.getSingleGame(command.gameID);
            String message = String.format("%s has joined the game as an observer!", userName);
            Notification notification = new Notification(message);
            connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
        } catch (IOException | DataAccessException e) {
            Error errorMessage = new Error(e.getMessage());
            connectionManager.sendError(command.gameID, command.getAuthString(), errorMessage);
        }
    }

    private void joinPlayer(Session session, String msg) throws IOException {
        JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
        Connection conn = new Connection(command.getAuthString(), session);
        connectionManager.add(command.gameID, conn);
        String playerColor;
        if (command.teamColor == ChessGame.TeamColor.BLACK) { playerColor = "black"; }
        else { playerColor = "white"; }
        try {
            String userName = authDAO.getUsername(new AuthToken(command.getAuthString()));
            ChessGame game = gameDAO.getSingleGame(command.gameID);
            String message = String.format("%s has joined the game as a %s player!", userName, playerColor);
            Notification notification = new Notification(message);
            LoadGame loadGame = new LoadGame(message);
            connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
            connectionManager.sendToSelf(command.gameID, command.getAuthString(), loadGame);
        } catch (IOException | DataAccessException e) {
            Error errorMessage = new Error(e.getMessage());
            connectionManager.sendError(command.gameID, command.getAuthString(), errorMessage);
        }
    }

    public void leave(Session session, String msg) throws IOException, DataAccessException {
        Leave command = new Gson().fromJson(msg, Leave.class);
        Connection conn = new Connection(command.getAuthString(), session);
        connectionManager.remove(command.gameID, conn);
        String userName = authDAO.getUsername(new AuthToken(command.getAuthString()));
        String message = String.format("%s has left the game!", userName);
        Notification notification = new Notification(message);
        connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
    }
    private void resign(Session session, String msg) throws IOException, DataAccessException {
        Resign command = new Gson().fromJson(msg, Resign.class);
        String userName = authDAO.getUsername(new AuthToken(command.getAuthString()));
        String message = String.format("%s has resigned from the game!", userName);
        Notification notification = new Notification(message);
        connectionManager.broadcast(command.gameID, command.getAuthString(), notification);
        connectionManager.removeGameID(command.gameID);
    }

    private void makeMove(Session session, String msg) throws IOException {
        MakeMove command = new Gson().fromJson(msg, MakeMove.class);
    }
}