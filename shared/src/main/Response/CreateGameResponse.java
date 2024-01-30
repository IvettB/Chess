package Response;

/**
 * Represents a response to a request to create a new game.
 */
public class CreateGameResponse {

    /**
     * The unique identifier (game ID) of the created game.
     */
    private Integer gameID;

    /**
     * A message describing the result or status of the create game operation.
     */
    private String message;

    /**
     * Constructs a CreateGameResponse with the provided game ID.
     * @param gameID The unique identifier (game ID) of the created game
     */
    public CreateGameResponse(Integer gameID) {
        this.gameID = gameID;
    }

    /**
     * Constructs a CreateGameResponse with the provided message
     * @param message A message describing the result or status of the create game operation
     */
    public CreateGameResponse(String message) {
        this.message = message;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }
}
