package Request;

/**
 * Represents a request to register a new user account.
 */
public class RegisterRequest {

    /**
     * The desired username for the new user account.
     */
    private final String username;

    /**
     * The password associated with the new user account.
     */
    private final String password;

    /**
     * The email address associated with the new user account.
     */
    private final String email;

    /**
     * Constructs a RegisterRequest with the provided username, password, and email
     * @param username The desired username for the new user account.
     * @param password The password associated with the new user account
     * @param email The email address associated with the new user account
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
