package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.AuthToken;
import request.LoginRequest;
import result.ErrorResult;
import result.UserResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var userInfo = new Gson().fromJson(request.body(), LoginRequest.class);
        UserService service = new UserService();
        try {
            AuthToken authToken = service.login(userInfo);
            UserResult resp = new UserResult(userInfo.username(), authToken.authToken());
            response.status(200);
            return new Gson().toJson(resp);
        } catch (DataAccessException e) {
            response.status(401);
            ErrorResult err = new ErrorResult(e.getMessage());
            response.body(new Gson().toJson(err));
            return new Gson().toJson(err);
        }
    }
}
