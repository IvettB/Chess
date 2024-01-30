package Handler;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Service.ClearService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Response;
import Response.ClearResponse;

import java.sql.Connection;

public class ClearHandler {
    public Object handle(Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        // Create an instance of ClearService with necessary DAO objects.
        ClearService myService = new ClearService(new AuthDAO(connection), new GameDAO(connection), new UserDAO(connection));
        ClearResponse myResponse = myService.clearApplication();
        response.type("application/json");

        // Check if the ClearService response contains "Success!" message.
        // If successful, set the HTTP status to 200 (OK); otherwise, set it to 500 (Internal Server Error).
        if (myResponse.getMessage().contains("Success!")) {
            response.status(200);
        }
        else {
            response.status(500);
        }
        database.returnConnection(connection);
        return new Gson().toJson(myResponse);
    }

}
