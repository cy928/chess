package handler;

import dataAccessError.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

public class ClearApplicationHandler implements Route {
    @Override
        public Object handle(Request request, Response response) throws SQLException, DataAccessException {
        ClearService service = new ClearService();
        service.delete();
        response.status(200);
        return "{}";
    }
}
