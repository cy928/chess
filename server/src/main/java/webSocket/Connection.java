package webSocket;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public Session session;
    public String authToken;

    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }
    @OnWebSocketMessage
    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}