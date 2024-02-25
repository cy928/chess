package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import response.ErrorResponse;
import response.ListGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGameHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        var auth = new AuthToken(request.headers("authorization"));
        GameService service = new GameService();
        try {
            ListGameResponse resp = service.getGameList(auth);
            response.status(200);
            response.body(new Gson().toJson(resp));
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            response.status(401);
            ErrorResponse err=new ErrorResponse(e.getMessage());
            response.body(new Gson().toJson(err));
            return new Gson().toJson(err);
        }
    }
}
