package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import result.ErrorResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        UserService service = new UserService();
        try {
            service.logout(authToken);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            response.status(401);
            ErrorResult err = new ErrorResult(e.getMessage());
            return new Gson().toJson(err);
        }

    }
}
