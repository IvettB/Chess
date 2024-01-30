package Handler;

import DataAccess.AuthDAO;
import Service.LogoutService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Request;
import spark.Response;
import Response.LogoutResponse;

import java.sql.Connection;

public class LogoutHandler {

    public Object handle(Request request, Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        String token = request.headers("authorization");
        LogoutService myService = new LogoutService(new AuthDAO(connection));
        LogoutResponse myResponse = myService.deleteSession(token);
        response.type("application/json");

        // Determine the appropriate HTTP status code based on the response message.
        if (myResponse.getMessage() == null) {
            response.status(200);
        }
        else if (myResponse.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        }
        else {
            response.status(500);
        }
        // Convert the LogoutResponse object to a JSON representation and return it as the response.
        database.returnConnection(connection);
        return new Gson().toJson(myResponse);
    }
}
