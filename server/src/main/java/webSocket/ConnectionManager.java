package webSocket;

import com.google.gson.Gson;
import serverMessages.Error;
import serverMessages.LoadGame;
import serverMessages.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Connection>> connectionsMap = new ConcurrentHashMap<>();

    public void add(Integer gameID, Connection connection) {
        if (connectionsMap.get(gameID) == null) {
            connectionsMap.put(gameID, new ArrayList<>());
        }
        connectionsMap.get(gameID).add(connection);
    }

    public void remove(Integer gameID, Connection connection) {
        List<Connection> connList = connectionsMap.get(gameID);
        if (connList != null) {
            connList.remove(connection);
            if (connList.isEmpty()) {
                connectionsMap.remove(gameID);
            }
        }
    }

    public void broadcast(Integer gameID, String excludeAuthToken, Notification msg) throws IOException {
        for (Connection conn : connectionsMap.get(gameID)) {
            if (conn.session.isOpen()) {
                if (!conn.authToken.equals(excludeAuthToken)) {
                    conn.send(new Gson().toJson(msg));
                }
            }
        }
    }

    public void sendToSelf(Integer gameID, String excludeAuthToken, LoadGame msg) throws IOException {
        for (Connection conn : connectionsMap.get(gameID)) {
            if (conn.session.isOpen()) {
                if (conn.authToken.equals(excludeAuthToken)) {
                    conn.send(new Gson().toJson(msg));
                }
            }
        }
    }
    public void sendGame(Integer gameID, LoadGame msg) throws IOException {
        for (Connection conn : connectionsMap.get(gameID)) {
            if (conn.session.isOpen()) {
                conn.send(new Gson().toJson(msg));
            }
        }
    }

    public void sendError(Connection conn, Error msg) throws IOException {
        if (conn.session.isOpen()) {
            conn.send(new Gson().toJson(msg));
        }
    }

    public  void removeGameID (Integer gameID) {
        connectionsMap.remove(gameID);
    }
}
