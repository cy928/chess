package handler;

import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import request.AuthToken;
import result.ErrorResult;
import result.UserResult;
import spark.Request;
import spark.Response;
import spark.Route;
import request.RegisterRequest;
import service.UserService;

import java.util.Objects;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var registerInfo = new Gson().fromJson(request.body(), RegisterRequest.class);
        UserService service = new UserService();
        try {
            AuthToken authToken = service.register(registerInfo);
            var resp = new UserResult(registerInfo.username(), authToken.authToken());
            response.status(200);
            response.body(new Gson().toJson(resp));
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")) {
                response.status(400);
            } else if (Objects.equals(e.getMessage(), "Error: already taken")) {
                response.status(403);
            }
            ErrorResult err = new ErrorResult(e.getMessage());
            response.body(new Gson().toJson(err));
            return new Gson().toJson(err);
        }
    }
}
