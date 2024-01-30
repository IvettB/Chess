package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Response.ListGamesResponse;
import dataAccess.DataAccessException;
import Model.Game;


/**
 * Service for listing available games.
 */
public class ListGamesService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;

    /**
     * Data Access Object for game-related data.
     */
    private final GameDAO gameDAO;


    /**
     * Constructs a ListGamesService with the specified data access objects.
     * @param authDAO      Data Access Object for user authentication information.
     * @param gameDAO      Data Access Object for game-related data.
     */
    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /**
     *  * Lists available games based on the provided request.
     * @param token The request object containing criteria for listing available games.
     * @return A ListGamesResponse indicating the outcome of the list games operation.
     */
    public ListGamesResponse listGames(String token) {
        try {
            if (authDAO.FindAuthtoken(token)) {
                return new ListGamesResponse("Error: unauthorized");
            }
            // Retrieve an array of available games from the game DAO and convert it to an array.
            Game[] games = gameDAO.FindAll().toArray(new Game[0]);
            // Return a ListGamesResponse containing the list of available games.
            return new ListGamesResponse(games);
        }
        catch(DataAccessException d) {
            return new ListGamesResponse(d.getMessage());
        }
    }
}
