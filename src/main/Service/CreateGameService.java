package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Model.Game;
import Request.CreateGameRequest;
import Response.CreateGameResponse;
import dataAccess.DataAccessException;
import java.util.Random;

/**
 * Service for creating a new game.
 */
public class CreateGameService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;

    /**
     * Data Access Object for game-related data
     */
    private final GameDAO gameDAO;


    /**
     * Constructs a CreateGameService with the specified data access objects.
     * @param authDAO Data Access Object for user authentication information
     * @param gameDAO Data Access Object for game-related data
     */
    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /**
     * Creates a new game based on the provided request.
     * @param request The request object containing information about the new game to be created.
     * @return A CreateGameResponse indicating the outcome of the game creation operation
     */
    public CreateGameResponse createGame(CreateGameRequest request, String token) {

        try {
            if (authDAO.FindAuthtoken(token)) {
                return new CreateGameResponse("Error: unauthorized");
            }
            // Generate a random game ID.
            Random rand = new Random();
            int gameID = rand.nextInt(100000);
            // Create a new game object with the generated ID and provided game name.
            Game newGame = new Game(gameID, null, null, request.getGameName(), new chess.Game());
            gameDAO.CreateGame(newGame);
            return new CreateGameResponse(gameID);
        }
        catch(DataAccessException d) {
            return new CreateGameResponse(d.getMessage());
        }
    }
}