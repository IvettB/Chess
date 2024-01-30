package Response;


/**
 * Represents a response to a user registration request.
 */
public class RegisterResponse {

    /**
     * The username of the newly registered user.
     */
    private String username;

    /**
     * An authentication token associated with the newly registered user.
     */
    private String authToken;

    /**
     * A message describing the result or status of the registration operation.
     */
    private String message;

    /**
     * Constructs a RegisterResponse with the provided username and authentication token.
     * @param username The username of the newly registered user
     * @param authToken An authentication token associated with the newly registered user
     */
    public RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;

    }

    /**
     * Constructs a RegisterResponse with the provided message.
     * @param message A message describing the result or status of the registration operation
     */
    public RegisterResponse(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }
}
