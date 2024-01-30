package Request;


/**
 * Represents a request to log in a user.
 */
public class LoginRequest {

    /**
     * The username of the user attempting to log in.
     */
    private final String username;

    /**
     * The password associated with the user's account.
     */
    private final String password;

    /**
     * Constructs a LoginRequest with the provided username and password
     * @param username The username of the user attempting to log in
     * @param password The password associated with the user's account
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
