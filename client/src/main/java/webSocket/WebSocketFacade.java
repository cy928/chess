package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import java.io.IOException;
import javax.websocket.*;
import java.net.URISyntaxException;
import java.net.URI;

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
            this.authToken = authToken;
            this.notificationHandler = notificationHandler;
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            this.session = webSocketContainer.connectToServer(this, webSocketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
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

    public void leave(Integer gameID) throws DataAccessException {
        try {
            Leave action = new Leave(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void resign(Integer gameID) throws DataAccessException {
        try {
            Resign action = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void makeMove(Integer gameID, ChessMove move) throws DataAccessException {
        try {
            MakeMove action = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void joinObserver(Integer gameID) throws DataAccessException {
        try {
            JoinObserver action = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void joinPlayer(Integer gameID, ChessGame.TeamColor color) throws DataAccessException {
        try {
            JoinPlayer action = new JoinPlayer(authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
