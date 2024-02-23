package handler;

import com.google.gson.Gson;
import request.AuthToken;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      var game_info = new Gson().fromJson(request.body(), JoinGameRequest.class);
      AuthToken authToken = new AuthToken(request.headers("authorization"));
      GameService.joinGame(authToken, game_info.playerColor(), game_info.gameID());
      response.status(200);
      return response;
    }
}
