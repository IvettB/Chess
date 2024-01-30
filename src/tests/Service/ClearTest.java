package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Model.Game;
import Response.ClearResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Model.User;
import java.sql.Connection;

public class ClearTest {
    private final Database database = new Database();
    private Connection connection;

    /**
     * This method is executed before each test case to set up the initial state.
     * It clears the application data by invoking the clearApplication method of the ClearService.
     */
    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        connection = database.getConnection();
        new ClearService(new AuthDAO(connection), new GameDAO(connection), new UserDAO(connection)).clearApplication();
    }

    @Test
    public void TestSuccess() throws DataAccessException {
        // Create and store test data (authorization token, game, and user).
        Authtoken auth = new Authtoken("halloween", "Jack45");
        new AuthDAO(connection).CreateAuthtoken(auth);
        Game game = new Game(16464, null, null, "chessy", null);
        new GameDAO(connection).CreateGame(game);
        User newUser = new User("Ivett112233", "choccymilk76", "ib@gmail.com");
        new UserDAO(connection).CreateUser(newUser);
        // Invoke the clearApplication method to clear the application data.
        ClearResponse response = new ClearService(new AuthDAO(connection), new GameDAO(connection), new UserDAO(connection)).clearApplication();

        // Assert that the data in each DAO is cleared (size is 0) after the clear operation.
        Assertions.assertNull(new AuthDAO(connection).ReadAuthtoken(auth.getAuthToken()));
        Assertions.assertNull(new GameDAO(connection).ReadGame(game.getGameID()));
        Assertions.assertNull(new UserDAO(connection).ReadUser(newUser.getUsername()));
    }
}