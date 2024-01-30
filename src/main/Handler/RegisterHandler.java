package Handler;

import DataAccess.AuthDAO;
import DataAccess.UserDAO;
import Service.RegisterService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Request;
import spark.Response;
import Request.RegisterRequest;
import Response.RegisterResponse;

import java.sql.Connection;

public class RegisterHandler {

    public Object handle(Request request, Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        // Deserialize the incoming request body to a RegisterRequest object using Gson.
        RegisterRequest myRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
        // Create an instance of RegisterService with required DAO objects.
        RegisterService myService = new RegisterService(new AuthDAO(connection), new UserDAO(connection));
        // Call the registerUser method to create a new user account and obtain a response.
        RegisterResponse myResponse = myService.registerUser(myRequest);
        response.type("application/json");

        if (myResponse.getMessage() == null) {
            response.status(200);
        }
        else if (myResponse.getMessage().equals("Error: bad request")) {
            response.status(400);
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
