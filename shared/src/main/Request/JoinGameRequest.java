package Request;

/**
 * Represents a request to join an existing chess game.
 * This class contains information required for a player to join an existing chess game
 */
public class JoinGameRequest {

    /**
     * The desired color of the player who wants to join the game
     */
    private final String playerColor;

    /**
     * This attribute represents the unique identifier (ID)
     * of the chess game that the player wishes to join.
     */
    private final int gameID;
    private String authtoken;

    public JoinGameRequest(String playerColor, int gameID, String authtoken) {
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.authtoken = authtoken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
