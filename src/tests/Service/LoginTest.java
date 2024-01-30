package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Request.LoginRequest;
import Response.LoginResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Model.User;
import java.sql.Connection;

public class LoginTest {
    private final Database database = new Database();
    private Connection connection;

    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        connection = database.getConnection();
        new ClearService(new AuthDAO(connection), new GameDAO(connection), new UserDAO(connection)).clearApplication();
    }

    @Test
    public void TestSucces() throws DataAccessException {
        // Create a new user and store it in the UserDAO.
        User newUser = new User("blabla", "bummer87", "ivb@gmail.com");
        new UserDAO(connection).CreateUser(newUser);
        LoginRequest request = new LoginRequest("blabla", "bummer87");
        LoginResponse response = new LoginService(new AuthDAO(connection), new UserDAO(connection)).createSession(request);

        Assertions.assertEquals("blabla", new AuthDAO(connection).ReadAuthtoken(response.getAuthToken()).getUsername());
    }

    @Test
    public void TestFail() throws DataAccessException {
        // Create a new user with a different password and store it in the UserDAO.
        User newUser = new User("blabla", "not good", "ivb@gmail.com");
        new UserDAO(connection).CreateUser(newUser);
        LoginRequest request = new LoginRequest("blabla", "good");
        LoginResponse response = new LoginService(new AuthDAO(connection), new UserDAO(connection)).createSession(request);

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }
}
