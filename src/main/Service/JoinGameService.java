package Service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Model.Authtoken;
import Request.JoinGameRequest;
import Response.JoinGameResponse;
import dataAccess.DataAccessException;
import Model.Game;

/**
 * Service for joining an existing game.
 */
public class JoinGameService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;

    /**
     * Data Access Object for game-related data.
     */
    private final GameDAO gameDAO;


    /**
     * Constructs a JoinGameService with the specified data access objects.
     * @param authDAO      Data Access Object for user authentication information.
     * @param gameDAO      Data Access Object for game-related data.
     */
    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /**
     * Joins an existing game.
     * @return A JoinGameResponse indicating the outcome of the join game operation
     */
    public JoinGameResponse joinGame(JoinGameRequest request, String token) {

        try {
            // Check if the requested game ID exists; if not, return a "bad request" response.
            if (!gameDAO.FindGameID(request.getGameID())) {
                return new JoinGameResponse("Error: bad request");
            }
            Authtoken myToken = authDAO.ReadAuthtoken(token);
            // If the authorization token is not found or is invalid, return an "unauthorized" response.
            if (myToken == null) {
                return new JoinGameResponse("Error: unauthorized");
            }

            if (request.getPlayerColor() == null) {
                return new JoinGameResponse();
            }
            // Retrieve the game based on the requested game ID.
            Game game = gameDAO.ReadGame(request.getGameID());
            if (request.getPlayerColor().equals("WHITE")) {
                if (game.getWhiteUsername()!=null) {
                    return new JoinGameResponse("Error: already taken");
                }
            }
            if (request.getPlayerColor().equals("BLACK")) {
                if (game.getBlackUsername()!=null) {
                    return new JoinGameResponse("Error: already taken");
                }
            }
            // Claim the player spot in the game using the user's token, player color, and game ID.
            gameDAO.ClaimSpot(myToken.getUsername(), request.getPlayerColor(), request.getGameID());
        }
        catch(DataAccessException d) {
            return new JoinGameResponse(d.getMessage());
        }
        return new JoinGameResponse();
    }
}
