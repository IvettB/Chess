package Response;
import Model.Game;

/**
 * Represents a response to a request to list available games.
 */
public class ListGamesResponse {
    /**
     * An array of successful game listing responses.
     */
    Game[] games;

    /**
     * A message describing the result or status of the list games operation.
     */
    private String message;

    /**
     * Constructs a ListGamesResponse with an array of successful game listing responses.
     * @param game An array of successful game listing responses.
     */
    public ListGamesResponse(Game[] game) {
        this.games = game;
    }

    /**
     * Constructs a ListGamesResponse with the provided message.
     * @param message A message describing the result or status of the list games operation
     */
    public ListGamesResponse(String message) {
        this.message = message;
    }

    public Game[] getGame() {
        return games;
    }

    public String getMessage() {
        return message;
    }
}
