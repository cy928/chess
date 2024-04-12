package handler;

import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import request.AuthToken;
import result.ErrorResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

import static handler.LoginHandler.handleError;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        AuthToken authToken = new AuthToken(request.headers("authorization"));
        UserService service = new UserService();
        try {
            service.logout(authToken);
            response.status(200);
            return "{}";
        } catch (DataAccessException | SQLException e) {
            return handleError(response, e);
        }

    }
}
