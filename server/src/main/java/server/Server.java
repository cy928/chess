package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) ->
                (new ClearApplicationHandler()).handleRequest(req,
                        res));
        Spark.post("/user", (req, res) ->
                (new RegisterHandler()).handleRequest(req,
                        res));
        Spark.post("/session", (req, res) ->
                (new LoginHandler()).handleRequest(req,
                        res));
        Spark.delete("/session", (req, res) ->
                (new LogoutHandler()).handleRequest(req,
                        res));
        Spark.get("/game", (req, res) ->
                (new ListGameHandler()).handleRequest(req,
                        res));
        Spark.post("/game", (req, res) ->
                (new CreatGameHandler()).handleRequest(req,
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
