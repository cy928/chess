package handler;

import request.AuthToken;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        UserService.logout(authToken);
        response.status(200);
        return response;
    }
}
