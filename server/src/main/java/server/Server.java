package server;

import spark.*;
import handler.*;
import webSocket.WebSocketHandler;

public class Server {
    WebSocketHandler webSocketHandler = new WebSocketHandler();
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.webSocket("/connect", webSocketHandler);
        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearApplicationHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.get("/game", new ListGameHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());

        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
