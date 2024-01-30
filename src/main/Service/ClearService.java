package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Response.ClearResponse;
import dataAccess.DataAccessException;


/**
 * Service for clearing application data.
 */
public class ClearService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;

    /**
     * Data Access Object for game-related data.
     */
    private final GameDAO gameDAO;

    /**
     * Data Access Object for user data.
     */
    private final UserDAO userDAO;

    /**
     * Constructs a ClearService with the specified data access objects.
     * @param authDAO Data Access Object for user authentication information.
     * @param gameDAO Data Access Object for game-related data
     * @param userDAO Data Access Object for user data
     */
    public ClearService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /**
     * Clears application data
     * @return A ClearResponse indicating the outcome of the clear operation
     */
    public ClearResponse clearApplication() {
        try {
            authDAO.Clear();
            gameDAO.Clear();
            userDAO.Clear();
        }
        catch(DataAccessException d) {
            return new ClearResponse(d.getMessage());
        }
        // Return a ClearResponse with a "Success!" message indicating successful data clearing.
        return new ClearResponse("Success!");
    }
}
