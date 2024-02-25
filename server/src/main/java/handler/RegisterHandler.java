package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import response.ErrorResponse;
import response.UserResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import request.RegisterRequest;
import service.UserService;

import java.util.Objects;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var register_info = new Gson().fromJson(request.body(), RegisterRequest.class);
        UserService service = new UserService();
        try {
            AuthToken authToken=service.register(register_info);
            var resp=new UserResponse(register_info.username(), authToken.authToken());
            response.status(200);
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")) {
                response.status(400);
            } else if (Objects.equals(e.getMessage(), "Error: already taken")) {
                response.status(403);
            }
            ErrorResponse err = new ErrorResponse(e.getMessage());
            response.body(new Gson().toJson(err));
            return new Gson().toJson(err);
        }
    }
}
