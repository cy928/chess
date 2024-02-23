package handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class ClearApplicationHandler implements Route {
    @Override
        public Object handle(Request request, Response response) throws Exception {
        gameService.deleteall();
        response.status(200);
        return response;
    }

}
