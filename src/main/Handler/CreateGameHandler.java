package Handler;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Service.CreateGameService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Request;
import spark.Response;
import Request.CreateGameRequest;
import Response.CreateGameResponse;

import java.sql.Connection;

public class CreateGameHandler {

    public Object handle(Request request, Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        CreateGameRequest myRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
        // Extract the authorization token from the request headers.
        String token = request.headers("authorization");
        CreateGameService myService = new CreateGameService(new AuthDAO(connection), new GameDAO(connection));
        CreateGameResponse myResponse = myService.createGame(myRequest, token);
        response.type("application/json");

        if (myResponse.getMessage() == null) {
            response.status(200);
        }
        else if (myResponse.getMessage().equals("Error: bad request")) {
            response.status(400);
        }
        else if (myResponse.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        }
        else {
            response.status(500);
        }
        // Convert the CreateGameResponse object to a JSON representation and return it as the response.
        database.returnConnection(connection);
        return new Gson().toJson(myResponse);
    }
}
