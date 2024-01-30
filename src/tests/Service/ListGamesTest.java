package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Model.Game;
import Response.ListGamesResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

public class ListGamesTest {

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
        Game game = new Game(1674, null, null, "chessy", null);
        new GameDAO(connection).CreateGame(game);
        // Invoke the listGames method to retrieve the list of available games for the user.
        ListGamesResponse response = new ListGamesService(new AuthDAO(connection), new GameDAO(connection)).listGames(auth.getAuthToken());

        // Assert that the response message is null (indicating success).
        Assertions.assertNull(response.getMessage());
        Assertions.assertEquals(1, response.getGame().length);
        // Assert that the game information in the response matches the created game's details.
        Assertions.assertEquals(game.getGameID(), response.getGame()[0].getGameID());
        Assertions.assertEquals(game.getGameName(), response.getGame()[0].getGameName());
        Assertions.assertNull(response.getGame()[0].getBlackUsername());
        Assertions.assertNull(response.getGame()[0].getWhiteUsername());
    }

    @Test
    public void TestFail() throws DataAccessException {
        Authtoken auth = new Authtoken("5336", "");
        new AuthDAO(connection).CreateAuthtoken(auth);
        ListGamesResponse response = new ListGamesService(new AuthDAO(connection), new GameDAO(connection)).listGames("joey");
        Assertions.assertEquals("Error: unauthorized",response.getMessage());
    }
}