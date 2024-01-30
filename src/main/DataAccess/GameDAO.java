package DataAccess;

import Model.Game;
import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.Connection;

/**
 * This class is responsible for accessing the data for Game
 */
public class GameDAO {

    private Connection connection;

    //private Database database = new Database();


    public GameDAO(Connection connection) {
        this.connection = connection;
    }

    public GameDAO() {
    }

    /**
     * A method for inserting a new game into the database.
     *
     * @param g takes in a parameter g for game
     * @throws DataAccessException throws an error if there is a database error
     */
    public void CreateGame(Game g) throws DataAccessException {
        //var connection = database.getConnection();
        String sql = "INSERT INTO Game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, g.getGameID());
            stmt.setString(2, g.getWhiteUsername());
            stmt.setString(3, g.getBlackUsername());
            stmt.setString(4, g.getGameName());

            var json = new Gson().toJson(g.getGame());
            stmt.setString(5, json);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error with inserting game: " + e.getMessage());
        }
//        finally {
//            database.returnConnection(connection);
//        }
    }

    /**
     * A method for retrieving a specified game from the database by gameID.
     *
     * @param gameID takes in an integer gameID as a parameter
     * @throws DataAccessException throws an error if there is a database error
     */
    public Game ReadGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM Game WHERE gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int myGameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");

                    var json = rs.getString("game");

                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessBoard.class, new boardAdapter());

                    var game = builder.create().fromJson(json, chess.Game.class);
                    return new Game(myGameID, whiteUsername, blackUsername, gameName, game);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    /**
     * A method/methods for claiming a spot in the game.
     * The player's username is provied and should be saved as either the whitePlayer or blackPlayer in the database.
     *
     * @param username takes in a username
     * @param color    takes in a teamcolor, either black or white
     * @throws DataAccessException throws an error if there is a database error
     */
    public void ClaimSpot(String username, String color, int gameID) throws DataAccessException {
        Game myGame = ReadGame(gameID);
        if (myGame == null) {
            throw new DataAccessException("Not valid");
        }
        if (color.equals("WHITE")) {
            try (var preparedStatement = connection.prepareStatement("UPDATE Game SET whiteUsername=? WHERE gameID=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } else if (color.equals("BLACK")) {
            try (var preparedStatement = connection.prepareStatement("UPDATE Game SET blackUsername=? WHERE gameID=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    public void removeGame(int gameID) throws DataAccessException {
        String sql = "DELETE FROM Game WHERE gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(Game g) throws DataAccessException {
        String sql = "UPDATE Game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, g.getWhiteUsername());
            stmt.setString(2, g.getBlackUsername());
            stmt.setString(3, g.getGameName());

            String json = new Gson().toJson(g.getGame());

            stmt.setString(4, json);
            stmt.setInt(5, g.getGameID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error with updating the game:" + e.getMessage());
        }
    }

    // Returns true or false based on weather gameID was found
    public boolean FindGameID(int gameID) throws DataAccessException {
        return ReadGame(gameID) != null;
    }

    // Returns a collection of all games
    public Collection<Game> FindAll() throws DataAccessException {
        String sql = "SELECT * FROM Game";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<Game> listGames = new ArrayList<>();
                while (rs.next()) {
                    int myGameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");

                    var json = rs.getString("game");

                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessGame.class, new gameAdapter());
                    builder.registerTypeAdapter(ChessBoard.class, new boardAdapter());
                    builder.registerTypeAdapter(ChessPiece.class, new pieceAdapter());

                    var game = builder.create().fromJson(json, ChessGame.class);
                    listGames.add(new Game(myGameID, whiteUsername, blackUsername, gameName, game));
                }
                return listGames;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * A method for clearing all data from the database
     *
     * @throws DataAccessException throws an error if there is a database error
     */
    public void Clear() throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement("DELETE FROM Game;")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    // TYPE ADAPTER CLASSES //

    static class gameAdapter implements JsonDeserializer<ChessGame> {
        public ChessGame deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return new Gson().fromJson(el, Game.class).getGame();
        }
    }

    static class boardAdapter implements JsonDeserializer<ChessBoard> {
        public ChessBoard deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {

            var builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new pieceAdapter());

            return builder.create().fromJson(el, Board.class);
        }
    }

    static class pieceAdapter implements JsonDeserializer<ChessPiece> {
        public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject jsonObject = el.getAsJsonObject();
            String typeString = jsonObject.get("type").getAsString();
            return switch (typeString) {
                case "KING" -> ctx.deserialize(el, King.class);
                case "QUEEN" -> ctx.deserialize(el, Queen.class);
                case "KNIGHT" -> ctx.deserialize(el, Knight.class);
                case "ROOK" -> ctx.deserialize(el, Rook.class);
                case "BISHOP" -> ctx.deserialize(el, Bishop.class);
                case "PAWN" -> ctx.deserialize(el, Pawn.class);
                default -> throw new JsonParseException("Unknown piece type: " + typeString);
            };
        }
    }
}