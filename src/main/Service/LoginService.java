package Service;

import DataAccess.AuthDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Request.LoginRequest;
import Response.LoginResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * Service for user login and session creation.
 */
public class LoginService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;

    /**
     * Data Access Object for user data.
     */
    private final UserDAO userDAO;

    /**
     * Constructs a LoginService with the specified data access objects.
     * @param authDAO      Data Access Object for user authentication information.
     * @param userDAO      Data Access Object for user data.
     */
    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    /**
     * Creates a user session based on the provided login request.
     * @param request The request object containing user login credentials
     * @return A LoginResponse indicating the outcome of the user login and session creation.
     */
    public LoginResponse createSession(LoginRequest request) {
        try {
            if(userDAO.ReadUser(request.getUsername()) == null) {
                return new LoginResponse("Error: unauthorized");
            }
            if(!Objects.equals(userDAO.ReadUser(request.getUsername()).getPassword(), request.getPassword())) {
                return new LoginResponse("Error: unauthorized");
            }
            // Generate a new authorization token for the user session.
            Authtoken newAuthToken = new Authtoken(UUID.randomUUID().toString(), request.getUsername());
            authDAO.CreateAuthtoken(newAuthToken);
            // Return a LoginResponse with the username and the newly created authorization token.
            return new LoginResponse(request.getUsername(), newAuthToken.getAuthToken());
        }
        catch(Exception x) {
            // If an exception occurs during the operation, return a LoginResponse with the error message.
            return new LoginResponse(x.getMessage());
        }
    }
}
