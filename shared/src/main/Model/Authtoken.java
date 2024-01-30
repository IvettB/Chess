package Model;

/**
 * Sets the structure for the Authtoken table in the database
 */
public class Authtoken {

    /**
     * The authorization token for a signed-in user
     */
    private final String authToken;

    /**
     * The user's unique identifier, a username string. Other players will identify the player by this name
     */
    private final String username;

    /**
     * Constructs the Authtoken object the user gets when logged into the game
     * @param authToken authorization token the user gets
     * @param username user's unique username
     */
    public Authtoken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
