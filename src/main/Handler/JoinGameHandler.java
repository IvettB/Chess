package Handler;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Service.JoinGameService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Request;
import spark.Response;
import Request.JoinGameRequest;
import Response.JoinGameResponse;

import java.sql.Connection;

public class JoinGameHandler {

    public Object handle(Request request, Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        JoinGameRequest myRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        String token = request.headers("authorization");
        JoinGameService myService = new JoinGameService(new AuthDAO(connection), new GameDAO(connection));
        // Call the joinGame method to attempt joining a game and obtain a response.
        JoinGameResponse myResponse = myService.joinGame(myRequest, token);
        response.type("application/json");

        // Determine the appropriate HTTP status code based on the response message.
        if (myResponse.getMessage() == null) {
            response.status(200);
        }
        else if (myResponse.getMessage().equals("Error: bad request")) {
            response.status(400);
        }
        else if (myResponse.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        }
        else if (myResponse.getMessage().equals("Error: already taken")) {
            response.status(403);
        }
        else {
            response.status(500);
        }
        database.returnConnection(connection);
        return new Gson().toJson(myResponse);
    }
}
