package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Model.User;
import Request.RegisterRequest;
import Response.RegisterResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

public class RegisterTest {
    private final Database database = new Database();
    private Connection connection;

    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        connection = database.getConnection();
        new ClearService(new AuthDAO(connection), new GameDAO(connection), new UserDAO(connection)).clearApplication();
    }

    @Test
    public void TestSuccess() throws DataAccessException {
        // Create a registration request with user information (username, password, and email).
        RegisterRequest request = new RegisterRequest("blabla", "bummer87", "ivb@gmail.com");
        RegisterResponse response = new RegisterService(new AuthDAO(connection), new UserDAO(connection)).registerUser(request);

        // Assert that the user with the registered username ("blabla") exists in the UserDAO.
        Assertions.assertNotNull(new UserDAO(connection).ReadUser(response.getUsername()));
    }

    @Test
    public void TestFail() throws DataAccessException {
        User newUser = new User("blabla", "bummer87", "ivb@gmail.com");
        new UserDAO(connection).CreateUser(newUser);
        RegisterRequest request = new RegisterRequest("blabla", "bummer87", "ivb@gmail.com");
        RegisterResponse response = new RegisterService(new AuthDAO(connection), new UserDAO(connection)).registerUser(request);

        // Assert that the response message is "Error: already taken" (indicating a failed registration due to a duplicate username).
        Assertions.assertEquals("Error: already taken", response.getMessage());
    }
}