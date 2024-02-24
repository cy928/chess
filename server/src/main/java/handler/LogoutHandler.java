package handler;

import dataAccess.DataAccessException;
import request.AuthToken;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        try {
            UserService.logout(authToken);
        } catch (DataAccessException e) {
            response.status(401);
            throw e;
        }
        response.status(200);
        return response;
    }
}
