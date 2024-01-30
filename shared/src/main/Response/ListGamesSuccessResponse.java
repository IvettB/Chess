package Response;

/**
 * Represents a successful response containing information about an available game.
 */
public class ListGamesSuccessResponse {

    private final int gameID;

    private final String gameName;

    /**
     * Constructs a ListGamesSuccessResponse with the provided game details.
     *
     * @param gameID The unique identifier (game ID) of the available game
     */
    public ListGamesSuccessResponse(int gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }
}
