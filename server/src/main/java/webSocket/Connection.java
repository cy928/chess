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
    @Override
    public boolean equals (Object object) {
        if (this == object) {
          return true;
        } else if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Connection that = (Connection) object;
        return authToken.equals(that.authToken) && session.equals(that.session);
    }
    @OnWebSocketMessage
    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}