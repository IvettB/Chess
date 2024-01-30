package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Request.CreateGameRequest;
import Response.ListGamesResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Response.CreateGameResponse;
import java.sql.Connection;

public class CreateGameTest {
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
        CreateGameRequest gameRequest = new CreateGameRequest("chessy");
        CreateGameResponse response = new CreateGameService(new AuthDAO(connection), new GameDAO(connection)).createGame(gameRequest, auth.getAuthToken());

        // Assert that the response message is null (indicating success).
        Assertions.assertNull(response.getMessage());
        // Assert that the response contains a non-null game ID and that the game is stored in the GameDAO.
        Assertions.assertNotNull(response.getGameID());
        Assertions.assertNotNull(new GameDAO(connection).ReadGame(response.getGameID()));
    }

    @Test
    public void TestFail() throws DataAccessException {
        // Create a new authorization token with a null password and store it in the AuthDAO.
        Authtoken auth = new Authtoken("5336", "");
        new AuthDAO(connection).CreateAuthtoken(auth);
        ListGamesResponse response = new ListGamesService(new AuthDAO(connection), new GameDAO(connection)).listGames("joey");
        // Assert that the response message is "Error: unauthorized".
        Assertions.assertEquals("Error: unauthorized",response.getMessage());
    }
}