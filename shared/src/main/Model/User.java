package Model;

/**
 * Sets the structure for the User table in the database
 */
public class User {

    /**
     * Stores the user's unique identifier username. Other player's will identify the player by this name
     */
    private final String username;

    /**
     * The password is a string that will be used to authenticate the user. This security measure will ensure
     * that only the user who created the userprofile will be able to log in with their username
     */
    private final String password;

    /**
     * The email is a string that will store the user's email. This will be used for authentication purposes as well
     */
    private final String email;

    /**
     * Constructs the user information that goes into the database
     * @param username the user's unique identifier name
     * @param password the user's secret password
     * @param email the user's email
     */
    public User(String username, String password, String email) {
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

    public String getEmail() {return email;}
}