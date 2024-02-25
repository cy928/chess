package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import request.JoinGameRequest;
import response.ErrorResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        var game_info = new Gson().fromJson(request.body(), JoinGameRequest.class);
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        GameService service = new GameService();
        try {
            service.joinGame(authToken, game_info);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request" )) {
                response.status(400);
            } else if(Objects.equals(e.getMessage(),"Error: unauthorized")){
                response.status(401);
            } else if(e.getMessage().equals("Error: already taken" )){
                response.status(403);}
            ErrorResponse err = new ErrorResponse(e.getMessage());
            response.body(new Gson().toJson(err));
            return new Gson().toJson(err);
        }
    }
}
