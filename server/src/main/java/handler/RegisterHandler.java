package handler;

import com.google.gson.Gson;
import request.AuthToken;
import response.userResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import request.RegisterRequest;
import service.userService;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var register_info = new Gson().fromJson(request.body(), RegisterRequest.class);
        AuthToken authToken = userService.register(register_info);
        var resp = new userResponse(register_info.username(), authToken);
        response.status(200);
        return new Gson().toJson(resp);
    }
}
