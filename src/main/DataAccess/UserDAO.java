package DataAccess;
import Model.User;
import dataAccess.DataAccessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for accessing the data for User
 */
public class UserDAO {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public UserDAO() {
    }

    /**
     * A method for inserting a new user into the database.
     * @param u takes in a user as a parameter
     * @throws DataAccessException throws an error if there is a database error
     */
    public void CreateUser(User u) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("INSERT INTO User (username, password, email) VALUES(?, ?, ?);")) {
            preparedStatement.setString(1, u.getUsername());
            preparedStatement.setString(2, u.getPassword());
            preparedStatement.setString(3, u.getEmail());

            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }    }

    /**
     * A method for retrieving a specified user from the database by username.
     * @param username takes in a string as a parameter
     */
    public User ReadUser(String username) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE username = ?;")) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                String myUsername = results.getString("username");
                String myPassword = results.getString("password");
                String myEmail = results.getString("email");
                return new User(myUsername, myPassword, myEmail);
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    // Gets the size of the map
//    public int GetSize() throws DataAccessException {
//        return myMap.size();
//    }

    // Returns true or false weather the user was found or not
    public boolean FindUser(String username) throws DataAccessException {
        return ReadUser(username) != null;
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException throws an error if there is a database error
     */
    public void Clear() throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("DELETE FROM User;")) {
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
