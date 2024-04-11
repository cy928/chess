package handler;

import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import request.AuthToken;
import request.CreateGameRequest;
import result.CreateGameResult;
import result.ErrorResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var gameInfo = new Gson().fromJson(request.body(), CreateGameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        GameService service = new GameService();
        try {
            CreateGameResult resp = service.createGame(authToken, gameInfo);
            response.status(200);
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request" )) {
                response.status(400);
            } else if (e.getMessage().equals("Error: unauthorized" )){
                response.status(401);
            }
            ErrorResult err = new ErrorResult(e.getMessage());
            return new Gson().toJson(err);
        }
    }
}
