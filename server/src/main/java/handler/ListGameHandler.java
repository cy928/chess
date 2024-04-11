package handler;

import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import request.AuthToken;
import result.ErrorResult;
import result.ListGameResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGameHandler implements Route{
    @Override
    public Object handle(Request request, Response response) {
        var authToken = new AuthToken(request.headers("authorization"));
        GameService service = new GameService();
        try {
            ListGameResult resp = service.getGameList(authToken);
            response.status(200);
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            response.status(401);
            ErrorResult err = new ErrorResult(e.getMessage());
            return new Gson().toJson(err);
        }
    }
}
