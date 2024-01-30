package Handler;

import DataAccess.AuthDAO;
import DataAccess.UserDAO;
import Service.LoginService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import spark.Request;
import spark.Response;
import Request.LoginRequest;
import Response.LoginResponse;

import java.sql.Connection;

// This method handles user login requests, creating a session and returning an appropriate response.
public class LoginHandler {

    public Object handle(Request request, Response response) throws DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        LoginRequest myRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService myService = new LoginService(new AuthDAO(connection), new UserDAO(connection));
        // Call the createSession method to authenticate the user and create a session, obtaining a response.
        LoginResponse myResponse = myService.createSession(myRequest);
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
