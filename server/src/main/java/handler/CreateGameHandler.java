package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import request.CreateGameRequest;
import response.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        var game_info = new Gson().fromJson(request.body(), CreateGameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        try {
            CreateGameResponse resp = GameService.createGame(authToken, game_info);
            response.status(200);
            response.body(new Gson().toJson(resp));
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
