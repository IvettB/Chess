package Service;

import DataAccess.AuthDAO;
import Response.LogoutResponse;
import dataAccess.DataAccessException;

/**
 * Service for user logout and session deletion.
 */
public class LogoutService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;


    /**
     * Constructs a LogoutService with the specified data access objects.
     * @param authDAO      Data Access Object for user authentication information.
     */
    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    /**
     * Deletes the user's session.
     * @return A LogoutResponse indicating the outcome of the user logout and session deletion
     */
    public LogoutResponse deleteSession(String token) {
        try {
            // Check if the provided authorization token is valid; if not, return an "unauthorized" response.
            if(authDAO.FindAuthtoken(token)) {
                return new LogoutResponse("Error: unauthorized");
            }
            // Delete the provided authorization token to log the user out.
            authDAO.DeleteAuthtoken(token);
        }
        catch(DataAccessException d) {
            return new LogoutResponse(d.getMessage());
        }
        return new LogoutResponse(null);
    }
}
