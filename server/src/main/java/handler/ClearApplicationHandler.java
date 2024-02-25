package handler;

import service.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearApplicationHandler implements Route {
    @Override
        public Object handle(Request request, Response response) {
        ClearService service = new ClearService();
        service.delete();
        response.status(200);
        return "{}";
    }
}
