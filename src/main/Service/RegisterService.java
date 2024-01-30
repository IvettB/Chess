package Service;

import DataAccess.AuthDAO;
import DataAccess.UserDAO;
import Model.Authtoken;
import Request.RegisterRequest;
import Response.RegisterResponse;
import Model.User;
import dataAccess.DataAccessException;

import java.util.UUID;

/**
 * Service for user registration.
 */
public class RegisterService {

    /**
     * Data Access Object for user authentication information.
     */
    private final AuthDAO authDAO;

    /**
     * Data Access Object for user data.
     */
    private final UserDAO userDAO;

    /**
     * Constructs a RegisterService with the specified data access objects.
     * @param authDAO      Data Access Object for user authentication information.
     * @param userDAO      Data Access Object for user data.
     */
    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user based on the provided registration request.
     * @param request The request object containing user registration details
     * @return A RegisterResponse indicating the outcome of the user registration.
     */
    public RegisterResponse registerUser(RegisterRequest request) {

        try {
            if (request == null) {
                return new RegisterResponse("Error: bad request");
            }
            if (request.getPassword() == null) {
                return new RegisterResponse("Error: bad request");
            }
            if (userDAO.FindUser(request.getUsername())) {
                return new RegisterResponse("Error: already taken");
            }
            else {
                // If none of the error conditions are met, create a new user and session for the user.
                User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail());
                userDAO.CreateUser(newUser);
                // Generate a new authorization token for the user's session.
                Authtoken newAuthToken = new Authtoken(UUID.randomUUID().toString(), request.getUsername());
                authDAO.CreateAuthtoken(newAuthToken);
                return new RegisterResponse(request.getUsername(), newAuthToken.getAuthToken());
            }
        }
        catch(DataAccessException d) {
            return new RegisterResponse(d.getMessage());
        }
    }
}