package DataAccess;

import Model.Authtoken;
import dataAccess.DataAccessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Responsible for accessing the data for Authtoken
 */
public class AuthDAO {

    private Connection connection;

    public AuthDAO(Connection connection) {
        this.connection = connection;
    }

    public AuthDAO() {
    }

    /**
     * Creates an Authtoken for a new user u
     * @param a     is the new authtoken
     * @throws DataAccessException  throws an error if there is a database error,
     *                              for example the username is already taken
     */
    public void CreateAuthtoken(Authtoken a) throws DataAccessException {
        //String sql = String.format("INSERT INTO Auth VALUES (\"%s\", \"%s\");" , a.getAuthToken(), a.getUsername());
        try (var preparedStatement = connection.prepareStatement("INSERT INTO Auth (Authtoken, Username) VALUES(?, ?);")) {
            preparedStatement.setString(1, a.getAuthToken());
            preparedStatement.setString(2, a.getUsername());

            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

//    public int GetSize() throws DataAccessException {
//        return myMap.size();
//    }

    /**
     * Reads Authtoken from the data store
     * @param authtoken     accepts a string authtoken
     * @throws DataAccessException throws an error if there is a database error
     */
    public Authtoken ReadAuthtoken(String authtoken) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("SELECT * FROM Auth WHERE Authtoken = ?;")) {
            preparedStatement.setString(1, authtoken);

            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                String token = results.getString("Authtoken");
                String username = results.getString("Username");
                Authtoken myAuthtoken = new Authtoken(token, username);
                return myAuthtoken;
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    // Returns true or false based on weather the authtoken is found
    public boolean FindAuthtoken(String authtoken) throws DataAccessException {
        return ReadAuthtoken(authtoken) == null;
    }

    /**
     * Deletes authtoken from the data store
     * @param authtoken     accepts a string authtoken
     * @throws DataAccessException  throws an error if there is a database error
     */
    public void DeleteAuthtoken(String authtoken) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("DELETE FROM Auth WHERE Authtoken = ?;")) {
            preparedStatement.setString(1, authtoken);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Clears the table in the database, deletes all the entries
     * @throws DataAccessException throws an error if there is a database error
     */
    public void Clear() throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("DELETE FROM Auth;")) {
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}