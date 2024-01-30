package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Model.Game;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Response.JoinGameResponse;
import Request.JoinGameRequest;
import java.sql.Connection;

public class JoinGameTest {
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
        // Create a request for a user to join the game with player color "BLACK" and game ID 1674.
        JoinGameRequest request = new JoinGameRequest("BLACK",1674, "pumpkin");
        // Invoke the joinGame method without providing an authorization token.
        JoinGameResponse response = new JoinGameService(new AuthDAO(connection), new GameDAO(connection)).joinGame(request, auth.getAuthToken());

        Assertions.assertEquals("Jimmy45", new GameDAO(connection).ReadGame(1674).getBlackUsername());
    }

    @Test
    public void TestFail() throws DataAccessException {
        Authtoken auth = new Authtoken("5336", "");
        new AuthDAO(connection).CreateAuthtoken(auth);
        JoinGameRequest request = new JoinGameRequest("BLACK",1674, "pumpkin");
        JoinGameResponse response = new JoinGameService(new AuthDAO(connection), new GameDAO(connection)).joinGame(request, "joey");

        Assertions.assertEquals("Error: bad request",response.getMessage());
    }
}