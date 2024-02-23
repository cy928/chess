package handler;

import com.google.gson.Gson;
import request.AuthToken;
import request.GameRequest;
import request.LoginRequest;
import response.UserResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreatGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var game_info = new Gson().fromJson(request.body(), GameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        gameID = GameService.creatGame(authToken, game_info.gameName());
        var resp = new UserResponse(user_info.username(), authToken);
        response.status(200);
        response.body(new Gson().toJson(resp));
        return new Gson().toJson(resp);
    }
}
