package Request;

/**
 * Represents the request to create a new game
 * This class encapsulates the necessary information to initiate the creation of a new game within a service.
 */
public class CreateGameRequest {

    /**
     * The name of the game that the service is requested to create.
     */
    private final String gameName;

    private String authtoken;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public CreateGameRequest(String gameName, String authtoken) {
        this.gameName = gameName;
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getGameName() {
        return gameName;
    }
}
