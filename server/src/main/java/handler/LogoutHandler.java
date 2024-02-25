package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import response.ErrorResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        UserService service = new UserService();
        try {
            service.logout(authToken);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            response.status(401);
            ErrorResponse err = new ErrorResponse(e.getMessage());
            response.body(new Gson().toJson(err));
            return new Gson().toJson(err);
        }

    }
}
