package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import response.UserResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws DataAccessException {
        var user_info = new Gson().fromJson(request.body(), LoginRequest.class);
        UserService service = new UserService();
        AuthToken authToken = service.login(user_info);
        UserResponse resp = new UserResponse(user_info.username(), authToken);
        response.status(200);
        response.body(new Gson().toJson(resp));
        return new Gson().toJson(resp);
    }
}
