package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Request.LogoutRequest;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Response.LogoutResponse;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;

public class LogoutTest {
    private final Database database = new Database();
    private Connection connection;

    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        connection = database.getConnection();
        new ClearService(new AuthDAO(connection), new GameDAO(connection), new UserDAO(connection)).clearApplication();
    }

    @Test
    public void TestSuccess() throws DataAccessException {
        Authtoken auth = new Authtoken("pumpkin", "Jimmy45");
        new AuthDAO(connection).CreateAuthtoken(auth);
        LogoutRequest request = new LogoutRequest("pumpkin");
        LogoutResponse response = new LogoutService(new AuthDAO(connection)).deleteSession(request.getAuthtoken());

        // Assert that the authorization token associated with "pumpkin" doesn't in the AuthDAO (indicating a successful logout).
        Assertions.assertTrue(new AuthDAO(connection).FindAuthtoken("pumpkin"));
    }

    @Test
    public void TestFail() throws DataAccessException {
        LogoutRequest request = new LogoutRequest("pumpkin");
        // Invoke the deleteSession method with an unauthorized or non-existent token.
        LogoutResponse response = new LogoutService(new AuthDAO(connection)).deleteSession(request.getAuthtoken());

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }
}