package Model;

import chess.ChessGame;

/**
 * Sets the structure for the Game table in the database
 */
public class Game {

    private boolean markEndOfGame = false;

    public boolean isMarkEndOfGame() {
        return markEndOfGame;
    }

    public void setMarkEndOfGame(boolean markEndOfGame) {
        this.markEndOfGame = markEndOfGame;
    }

    /**
     * gameID is a unique ID that identifies a specific game. Each game will have a unique ID,
     * which is different from the gameName
     */
    private int gameID;
    /**
     * White player's username will be stored in this parameter. It will be the player's unique identifier name
     * who will play with the white characters
     */
    private String whiteUsername;
    /**
     * Black player's username will be stored in this parameter. It will be the player's unique identifier name
     * who will play with the black characters
     */
    private String blackUsername;
    /**
     * gameName is the specific game's name that is being played by the two users
     */
    private final String gameName;
    /**
     * The implementation of the game
     */
    public ChessGame game;

    /**
     * Constructs the Game object when two users log in and want to play a multiplayer chess game
     *
     * @param gameID        the game's integer identifier number
     * @param whiteUsername username of whoever plays white
     * @param blackUsername username of whoever plays black
     * @param gameName      the game's name that is being played by the two users
     * @param game          the implementation of the game
     */
    public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
