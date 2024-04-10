package webSocket;

import com.google.gson.Gson;
import serverMessages.Error;
import serverMessages.LoadGame;
import serverMessages.Notification;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Connection>> connectionsList = new ConcurrentHashMap<>();

    public void add(Integer gameID, Connection connection) { connectionsList.get(gameID).add(connection); }

    public void remove(Integer gameID, Connection connection) {
        connectionsList.get(gameID).remove(connection);
    }

    public void broadcast(Integer gameID, String excludeAuthToken, Notification notification) throws IOException {
        for (var conn : connectionsList.get(gameID)) {
            if (conn.session.isOpen()) {
                if (!conn.authToken.equals(excludeAuthToken)) {
                    conn.send(new Gson().toJson(notification));
                }
            }
        }
    }

    public void sendToSelf(Integer gameID, String excludeAuthToken, LoadGame notification) throws IOException {
        for (var conn : connectionsList.get(gameID)) {
            if (conn.session.isOpen()) {
                if (conn.authToken.equals(excludeAuthToken)) {
                    conn.send(new Gson().toJson(notification));
                }
            }
        }
    }

    public void sendError(Integer gameID, String excludeAuthToken, Error notification) throws IOException {
        for (var conn : connectionsList.get(gameID)) {
            if (conn.session.isOpen()) {
                if (conn.authToken.equals(excludeAuthToken)) {
                    conn.send(new Gson().toJson(notification));
                }
            }
        }
    }

    public  void removeGameID (Integer gameID) {
        connectionsList.remove(gameID);
    }
}
