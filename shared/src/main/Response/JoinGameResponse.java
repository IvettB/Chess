package Response;

/**
 * Represents a response to a request to join an existing game
 */
public class JoinGameResponse {

    /**
     * The response message describing the outcome of the join game operation
     */
    private String message;

    /**
     * Constructs a JoinGameResponse with the provided message.
     * @param message The response message describing the outcome of the join game operation
     */
    public JoinGameResponse(String message) {
        this.message = message;
    }
    public JoinGameResponse() {}

    public String getMessage() {
        return message;
    }
}
