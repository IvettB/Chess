package DataAccess;
import Model.User;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOTest {

    private final Database database = new Database();
    private UserDAO userDAO;

    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        Connection connection = database.getConnection();
        userDAO = new UserDAO(connection);
        userDAO.Clear();
    }

    // CREATE USER BEGIN

    @Test
    void CreateUserSuccess() throws DataAccessException {
        var user = new User("pamela", "pamelapumpkin", "pamela@gmail.com");
        userDAO.CreateUser(user);
        assertEquals("pamelapumpkin", userDAO.ReadUser("pamela").getPassword());
        assertEquals("pamela", userDAO.ReadUser("pamela").getUsername());
        assertEquals("pamela@gmail.com", userDAO.ReadUser("pamela").getEmail());
    }

    @Test
    void CreateUserFail() throws DataAccessException {
        var user = new User("pamela", "pamelapumpkin", "pamela@gmail.com");
        userDAO.CreateUser(user);
        assertNull(userDAO.ReadUser("joey67"));
    }

    // CREATE USER END

    // READ USER BEGIN

    @Test
    void ReadUserSuccess() throws DataAccessException {
        var user = new User("pamela", "pamelapumpkin", "pamela@gmail.com");
        userDAO.CreateUser(user);
        assertEquals("pamelapumpkin", userDAO.ReadUser("pamela").getPassword());
        assertEquals("pamela", userDAO.ReadUser("pamela").getUsername());
        assertEquals("pamela@gmail.com", userDAO.ReadUser("pamela").getEmail());
    }

    @Test
    void ReadUserFail() throws DataAccessException {
        var user = new User("pamela", "pamelapumpkin", "pamela@gmail.com");
        userDAO.CreateUser(user);
        assertNull(userDAO.ReadUser("joey67"));
    }

    // READ USER END

    // FIND USER BEGIN

    @Test
    void FindUserSuccess() throws DataAccessException {
        var user = new User("pamela", "pamelapumpkin", "pamela@gmail.com");
        userDAO.CreateUser(user);
        assertTrue(userDAO.FindUser("pamela"));
    }

    @Test
    void FindUserFail() throws DataAccessException {
        var user = new User("pamela", "pamelapumpkin", "pamela@gmail.com");
        userDAO.CreateUser(user);
        assertFalse(userDAO.FindUser("joey67"));
    }

    // FIND USER END

    // CLEAR BEGIN

    @Test
    void ClearSuccess() throws DataAccessException {
        var user1 = new User("pamela79", "pamelapumpkin", "pam@gmail.com");
        var user2 = new User("joey67", "joeyjoe", "joey@yahoo.com");
        userDAO.CreateUser(user1);
        userDAO.CreateUser(user2);
        userDAO.Clear();
        assertFalse(userDAO.FindUser("pamela79"));
        assertFalse(userDAO.FindUser("joey67"));
    }

    // CLEAR END
}
