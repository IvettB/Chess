package Request;

/**
 * This class is currently being left blank intentionally.
 * As of now, it is likely it will not be used.
 */
public class LogoutRequest {

    String authtoken;

    public LogoutRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }
}
