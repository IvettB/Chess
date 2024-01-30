package DataAccess;

import Model.Game;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    private final Database database = new Database();
    private GameDAO gameDAO;


    @BeforeEach
    public void BeforeEach() throws DataAccessException {
        Connection connection = database.getConnection();
        gameDAO = new GameDAO(connection);
        gameDAO.Clear();
    }
    // CREATE GAME BEGIN
    @Test
    void CreateGameSuccess() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        assertTrue(gameDAO.FindGameID(10000));
        assertNull(gameDAO.ReadGame(10000).getWhiteUsername());
        assertNull(gameDAO.ReadGame(10000).getBlackUsername());
        assertEquals("blitz", gameDAO.ReadGame(10000).getGameName());
    }

    @Test
    void CreateGameFail() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(-1, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        assertThrows(DataAccessException.class, () -> gameDAO.CreateGame(game));
    }
    // CREATE GAME END

    // READ GAME BEGIN
    @Test
    void ReadGameSuccess() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        assertTrue(gameDAO.FindGameID(10000));
        assertNull(gameDAO.ReadGame(10000).getWhiteUsername());
        assertNull(gameDAO.ReadGame(10000).getBlackUsername());
        assertEquals("blitz", gameDAO.ReadGame(10000).getGameName());
    }

    @Test
    void ReadGameFail() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        assertNull(gameDAO.ReadGame(10000));
    }
    // READ GAME END

    // CLAIM SPOT BEGIN
    @Test
    void ClaimSpotSuccess() throws Exception {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        gameDAO.ClaimSpot("joe", "WHITE", 10000);
        assertEquals("joe", gameDAO.ReadGame(10000).getWhiteUsername());
    }

    @Test
    void ClaimSpotFail() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        gameDAO.ClaimSpot(null, "WHITE", 10000);
        assertThrows(DataAccessException.class, () -> gameDAO.ClaimSpot(null, "WHITE", -1));
    }
    // CLAIM SPOT END

    // FIND GAME ID BEGIN
    @Test
    void FindGameIDSuccess() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        assertTrue(gameDAO.FindGameID(10000));
    }

    @Test
    void FindGameIDFail() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        assertFalse(gameDAO.FindGameID(10000));
    }
    // FIND GAME ID END

    // FIND ALL BEGIN
    @Test
    void FindAllSuccess() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game1 = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game1);
        var game2 = new Game(10001, null, null, "mars",cg );
        gameDAO.CreateGame(game2);
        var game3 = new Game(10002, null, null, "jupiter",cg );
        gameDAO.CreateGame(game3);
        assertEquals(3, gameDAO.FindAll().size());
    }

    @Test
    void FindAllFail() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game1 = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game1);
        var game2 = new Game(10001, null, null, "mars",cg );
        gameDAO.CreateGame(game2);
        var game3 = new Game(10002, null, null, "jupiter",cg );
        gameDAO.CreateGame(game3);
        assertNotEquals(2, gameDAO.FindAll().size());
    }
    // FIND ALL END

    // CLEAR BEGIN
    @Test
    void ClearSuccess() throws DataAccessException {
        var cg = new chess.Game();
        var board = new chess.Board();
        board.resetBoard();
        cg.setBoard(board);
        var game = new Game(10000, null, null, "blitz",cg );
        gameDAO.CreateGame(game);
        gameDAO.Clear();
        assertEquals(0, gameDAO.FindAll().size());
    }
    // CLEAR END
}