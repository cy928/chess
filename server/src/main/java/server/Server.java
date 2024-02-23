package server;

import spark.*;
import handler.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearApplicationHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", (req, res) ->
                (new LogoutHandler()).handleRequest(req,
                        res));
        Spark.get("/game", (req, res) ->
                (new ListGameHandler()).handleRequest(req,
                        res));
        Spark.post("/game", (req, res) ->
                (new CreateGameHandler()).handleRequest(req,
                        res));
        Spark.put("/game", (req, res) ->
                (new JoinGameHandler()).handleRequest(req,
                        res));





        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
