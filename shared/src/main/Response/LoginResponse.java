package Response;


/**
 * Represents a response to a user login request.
 */
public class LoginResponse {


    /**
     * The username of the user who has successfully logged in.
     */
    private String username;

    /**
     * An authentication token associated with the user's login.
     */
    private String authToken;

    /**
     * A message describing the result or status of the login operation.
     */
    private String message;

    /**
     * Constructs a LoginResponse with the provided username and authentication token.
     *
     * @param username  The username of the user who has successfully logged in.
     * @param authToken An authentication token associated with the user's login
     */
    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    /**
     * onstructs a LoginResponse with the provided message.
     *
     * @param message A message describing the result or status of the login operation.
     */
    public LoginResponse(String message) {
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

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
