package handler;

import com.google.gson.Gson;
import request.AuthToken;
import request.LoginRequest;
import response.userResponse;
import service.userService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var user = new Gson().fromJson(request.body(), LoginRequest.class);
        AuthToken authToken = userService.login(user);
        var resp = new userResponse(user.username(), authToken);
        response.status(200);
        return new Gson().toJson(resp);
    }
}
