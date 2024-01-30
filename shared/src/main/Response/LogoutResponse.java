package Response;


/**
 * Represents a response to a user logout request.
 */
public class LogoutResponse {

    /**
     * A message describing the result or status of the logout operation.
     */
    private final String message;

    /**
     * Constructs a LogoutResponse with the provided message.
     * @param message A message describing the result or status of the logout operation.
     */
    public LogoutResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
