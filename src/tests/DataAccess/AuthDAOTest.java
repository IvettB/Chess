package DataAccess;

import Model.Authtoken;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {

    private final Database database = new Database();
    private AuthDAO authDAO;

    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        Connection connection = database.getConnection();
        authDAO = new AuthDAO(connection);
        authDAO.Clear();
    }

    @Test
    void CreateAuthtokenSuccess() throws DataAccessException {
        var auth = new Authtoken("ten-thousand", "blitz");
        authDAO.CreateAuthtoken(auth);
        assertEquals("blitz", authDAO.ReadAuthtoken("ten-thousand").getUsername());
        assertEquals("ten-thousand", authDAO.ReadAuthtoken("ten-thousand").getAuthToken());
    }

    @Test
    void CreateAuthtokenFail() throws DataAccessException {
        var auth = new Authtoken("joe", "blitz");
        authDAO.CreateAuthtoken(auth);
        assertNull(authDAO.ReadAuthtoken("ten-thousand"));
    }

    @Test
    void ReadAuthtokenSucces() throws DataAccessException {
        var auth = new Authtoken("ten-thousand", "blitz");
        authDAO.CreateAuthtoken(auth);
        assertEquals("blitz", authDAO.ReadAuthtoken("ten-thousand").getUsername());
        assertEquals("ten-thousand", authDAO.ReadAuthtoken("ten-thousand").getAuthToken());
    }

    @Test
    void ReadAuthtokenFail() throws DataAccessException {
        var auth = new Authtoken("joe", "blitz");
        authDAO.CreateAuthtoken(auth);
        assertNull(authDAO.ReadAuthtoken("ten-thousand"));
    }

    @Test
    void FindAuthtokenSuccess() throws DataAccessException {
        var auth = new Authtoken("ten-thousand", "blitz");
        authDAO.CreateAuthtoken(auth);
        assertFalse(authDAO.FindAuthtoken("ten-thousand"));
    }

    @Test
    void FindAuthtokenFail() throws DataAccessException {
        var auth = new Authtoken("ten-thousand", "blitz");
        authDAO.CreateAuthtoken(auth);
        assertTrue(authDAO.FindAuthtoken("joey"));
    }

    @Test
    void DeleteAuthtokenSuccess() throws DataAccessException {
        var auth = new Authtoken("ten-thousand", "blitz");
        authDAO.CreateAuthtoken(auth);
        authDAO.DeleteAuthtoken("ten-thousand");
        assertTrue(authDAO.FindAuthtoken("ten-thousand"));
    }

    @Test
    void DeleteAuthFail() throws DataAccessException {
        var auth = new Authtoken("ten-thousand", "blitz");
        authDAO.CreateAuthtoken(auth);
        authDAO.DeleteAuthtoken("joey");
        assertFalse(authDAO.FindAuthtoken("ten-thousand"));
    }

    @Test
    void ClearSuccess() throws DataAccessException {
        var auth1 = new Authtoken("ten-thousand", "blitz");
        var auth2 = new Authtoken("joey", "blitz");
        authDAO.CreateAuthtoken(auth1);
        authDAO.CreateAuthtoken(auth2);
        authDAO.Clear();
        assertTrue(authDAO.FindAuthtoken("ten-thousand"));
        assertTrue(authDAO.FindAuthtoken("joey"));
    }
}
