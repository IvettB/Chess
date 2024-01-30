package Response;

/**
 * Represents a response to a request to clear or reset an operation.
 */
public class ClearResponse {

    /**
     * The response message describing the outcome of the clearing operation
     */
    private final String message;

    /**
     * Constructs a ClearResponse with the provided message.
     * @param message The response message describing the outcome of the clearing operation
     */
    public ClearResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
