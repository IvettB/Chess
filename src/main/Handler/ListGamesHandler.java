package Handler;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Server.Server;
import Service.ListGamesService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Request;
import spark.Response;
import Response.ListGamesResponse;

import java.sql.Connection;

public class ListGamesHandler {

    public Object handle(Request request, Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        String token = request.headers("authorization");

//        Connection connection = null;
//        try {
//            connection = Server.database.getConnection();
//
//        } catch (DataAccessException e) {
//            response.status(500);
//            return new Gson().toJson(Map.of("message", "Error: error accessing database"));
//        }

        // Create an instance of ListGamesService with required DAO objects.
        ListGamesService myService = new ListGamesService(new AuthDAO(connection), new GameDAO(connection));
        Server.database.returnConnection(connection);
        ListGamesResponse myResponse = myService.listGames(token);
        // Set the response type to JSON.
        response.type("application/json");

        if (myResponse.getMessage() == null) {
            response.status(200);
        }
        else if (myResponse.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        }
        else {
            response.status(500);
        }
        database.returnConnection(connection);
        return new Gson().toJson(myResponse);
    }
}
