package handler;

import com.google.gson.Gson;
import request.AuthToken;
import request.ListGameRequest;
import response.ListGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var auth = new AuthToken(request.headers("authorization"));
        ListGameResponse resp = GameService.getGameList(auth);
        response.status(200);
        response.body(new Gson().toJson(resp));
        return new Gson().toJson(resp);
    }
}
