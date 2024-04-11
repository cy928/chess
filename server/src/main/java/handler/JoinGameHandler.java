package handler;

import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import request.AuthToken;
import request.JoinGameRequest;
import result.ErrorResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var gameInfo = new Gson().fromJson(request.body(), JoinGameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        GameService service = new GameService();
        try {
            service.joinGame(authToken, gameInfo);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request" )) {
                response.status(400);
            } else if (Objects.equals(e.getMessage(),"Error: unauthorized")) {
                response.status(401);
            } else if (e.getMessage().equals("Error: already taken" )) {
                response.status(403);}
            ErrorResult err = new ErrorResult(e.getMessage());
            return new Gson().toJson(err);
        }
    }
}
