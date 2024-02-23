package handler;

import com.google.gson.Gson;
import request.AuthToken;
import request.CreateGameRequest;
import response.CreateGameResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var game_info = new Gson().fromJson(request.body(), CreateGameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        CreateGameResponse resp = GameService.creatGame(authToken, game_info.gameName());
        response.status(200);
        response.body(new Gson().toJson(resp));
        return new Gson().toJson(resp);
    }
}
