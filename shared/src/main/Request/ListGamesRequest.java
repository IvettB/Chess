package Request;

/**
 * This class is currently being left blank intentionally.
 * As of now, it is likely it will not be used.
 */
public class ListGamesRequest {

    private String authtoken;

    public ListGamesRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }
}
