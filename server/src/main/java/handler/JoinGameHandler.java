package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import request.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        var game_info = new Gson().fromJson(request.body(), JoinGameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        try {
            GameService.joinGame(authToken, game_info);
        } catch (DataAccessException e) {
            throw e;
        }
        response.status(200);
        return response;
    }
}
