package websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import java.io.IOException;
import javax.websocket.*;
import java.net.URISyntaxException;
import java.net.URI;

import ui.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

public class WebSocketFacade extends Endpoint {
    NotificationHandler notificationHandler;
    Session session;
    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI webSocketURI = new URI(url + "/connect");
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

    public void leave(Integer gameID) throws DataAccessException, ResponseException {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
