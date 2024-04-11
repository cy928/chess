package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import java.io.IOException;
import javax.websocket.*;
import java.net.URISyntaxException;
import java.net.URI;

import serverMessages.Error;
import serverMessages.LoadGame;
import serverMessages.Notification;
import ui.BoardDrawing;
import ui.GamePlayUI;
import userGameCommand.*;
import webSocketMessages.serverMessages.ServerMessage;

public class WebSocketFacade extends Endpoint {
    NotificationHandler notificationHandler;
    Session session;
    static String authToken;
    public WebSocketFacade(String url, NotificationHandler notificationHandler, String authToken) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI webSocketURI = new URI(url + "/connect");
            WebSocketFacade.authToken= authToken;
            this.notificationHandler = notificationHandler;
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            this.session = webSocketContainer.connectToServer(this, webSocketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage result = new Gson().fromJson(message, ServerMessage.class);
                    if (result.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        Notification notification = new  Gson().fromJson(message, Notification.class);
                        notificationHandler.notify(notification.getMessage());
                    } else if (result.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                        GamePlayUI.chessGame = loadGame.game;
                        GamePlayUI.drawing.updateBoard(loadGame.game);
                        if (GamePlayUI.teamColor == ChessGame.TeamColor.BLACK) {
                            BoardDrawing.printWholeBoard(BoardDrawing.board, ChessGame.TeamColor.BLACK);
                        } else if (GamePlayUI.teamColor == ChessGame.TeamColor.WHITE) {
                            BoardDrawing.printWholeBoard(BoardDrawing.board, ChessGame.TeamColor.WHITE);
                        }
                    } else if (result.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        Error errorMessage = new Gson().fromJson(message, Error.class);
                    }
                }
            }
            );
        } catch (DeploymentException | IOException | URISyntaxException ex) {
          throw new DataAccessException( ex.getMessage());
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void leave(Integer gameID, ChessGame.TeamColor teamColor) throws DataAccessException {
        try {
            Leave command = new Leave(authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void resign(Integer gameID) throws DataAccessException {
        try {
            Resign command = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void makeMove(Integer gameID, ChessMove move) throws DataAccessException {
        try {
            MakeMove command = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void joinObserver(Integer gameID) throws DataAccessException {
        try {
            JoinObserver command = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void joinPlayer(Integer gameID, ChessGame.TeamColor color) throws DataAccessException {
        try {
            JoinPlayer command = new JoinPlayer(authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
