package Server;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Model.Authtoken;
import Model.Game;
import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Collection;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer action2 = new Gson().fromJson(message, JoinPlayer.class);
                JoinPlayer(session, action2.getAuthString(), action2.getGameID(), action2.getTeamColor());
            }
            case JOIN_OBSERVER -> {
                JoinObserver action3 = new Gson().fromJson(message, JoinObserver.class);
                JoinObserver(session, action3.getAuthString(), action3.getGameID());
            }
            case MAKE_MOVE -> {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(ChessMove.class, new moveAdapter()).create();
                builder.registerTypeAdapter(ChessPosition.class, new posAdapter()).create();
                MakeMove action4 = builder.create().fromJson(message, MakeMove.class);
                MakeMove(session, action4.getAuthString(), action4.getGameID(), action4.getMove());
            }
            case LEAVE -> {
                Leave action5 = new Gson().fromJson(message, Leave.class);
                Leave(session, action5.getAuthString(), action5.getGameID());
            }
            case RESIGN -> {
                Resign action6 = new Gson().fromJson(message, Resign.class);
                Resign(session, action6.getAuthString(), action6.getGameID());
            }
        }
    }

    public void JoinPlayer(Session session, String authtoken, Integer gameID, ChessGame.TeamColor playerColor) throws DataAccessException, IOException {
        Database database = new Database();
        Connection connection = database.getConnection();
        AuthDAO authDAO = new AuthDAO(connection);
        GameDAO gameDAO = new GameDAO(connection);
        Game myGame = gameDAO.ReadGame(gameID);
        Authtoken myToken = authDAO.ReadAuthtoken(authtoken);
        Gson gson = new Gson();
        if (myGame != null && myToken != null) {
            // create the root client that interacts with join
            String rootClient = myToken.getUsername();

            // make sure that an observer can't join as a player
            if ((Objects.equals(myGame.getBlackUsername(), rootClient) && playerColor == ChessGame.TeamColor.BLACK) ||
                    (Objects.equals(myGame.getWhiteUsername(), rootClient) && playerColor == ChessGame.TeamColor.WHITE)) {

                connections.add(gameID, session, authtoken, rootClient, playerColor);
                // update the game
                gameDAO.updateGame(myGame);
                LoadGame loadGame = new LoadGame(myGame, rootClient);
                String loadJson = gson.toJson(loadGame);
                if (session.isOpen()) {
                    session.getRemote().sendString(loadJson);
                }

                String notification = String.format("%s joined game as %s", rootClient, playerColor);
                Notification newNotification = new Notification(notification);
                connections.broadcast(gameID, gson.toJson(newNotification), rootClient);
            } else {
                ErrorMessage newError = new ErrorMessage("Sorry, already taken");
                String errorJson = gson.toJson(newError);
                if (session.isOpen()) {
                    session.getRemote().sendString(errorJson);
                }
            }

        } else {
            ErrorMessage newError = new ErrorMessage("Bad input");
            session.getRemote().sendString(gson.toJson(newError));
        }
    }

    public void JoinObserver(Session session, String authtoken, Integer gameID) throws IOException, DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        AuthDAO authDAO = new AuthDAO(connection);
        GameDAO gameDAO = new GameDAO(connection);
        Game myGame = gameDAO.ReadGame(gameID);
        Authtoken myToken = authDAO.ReadAuthtoken(authtoken);
        Gson gson = new Gson();
        if (myGame != null && myToken != null) {
            String username = myToken.getUsername();
            // for observers, player color is null
            connections.add(gameID, session, authtoken, username, null);
            LoadGame loadGame = new LoadGame(myGame, username);
            String loadGameJson = gson.toJson(loadGame);
            if (session.isOpen()) {
                session.getRemote().sendString(loadGameJson);
            }
            String notification = String.format("%s joined as an observer", username);
            Notification newNotification = new Notification(notification);
            connections.broadcast(gameID, gson.toJson(newNotification), username);
        } else {
            ErrorMessage newError = new ErrorMessage("Bad input");
            session.getRemote().sendString(gson.toJson(newError));
        }
    }

    public void MakeMove(Session session, String authtoken, Integer gameID, ChessMove move) throws IOException, DataAccessException {
        try {
            Database database = new Database();
            Connection connection = database.getConnection();
            AuthDAO authDAO = new AuthDAO(connection);
            GameDAO gameDAO = new GameDAO(connection);
            Game myGame = gameDAO.ReadGame(gameID);
            Authtoken myToken = authDAO.ReadAuthtoken(authtoken);
            Gson gson = new Gson();
            // make sure it's not the end of the game
            if (myGame != null && !myGame.getGame().isMarkEndOfGame()) {
                // make sure it's the player color's turn
                if (isPlayerTurn(myToken, myGame)) {
                    String rootClient = myToken.getUsername();
                    // make sure it's a valid move
                    if (isValidMove(move, myGame)) {
                        myGame.getGame().makeMove(move);
                        gameDAO.updateGame(myGame);
                        LoadGame loadGame = new LoadGame(myGame, myToken.getUsername());
                        String loadGameJson = gson.toJson(loadGame);
                        connections.broadcast(gameID, loadGameJson, "");

                        if (session.isOpen()) {
                            session.getRemote().sendString(loadGameJson);
                        }
                        String notification = String.format("%s was moved to %s", myGame.getGame().getBoard().getPiece(move.getEndPosition()).toString(), move.getEndPosition().toString());
                        Notification newNotification = new Notification(notification);
                        connections.broadcast(gameID, gson.toJson(newNotification), rootClient);

                    } else {
                        ErrorMessage newError = new ErrorMessage("Invalid move");
                        if (session.isOpen()) {
                            session.getRemote().sendString(gson.toJson(newError));
                        }
                    }
                } else {
                    ErrorMessage newError = new ErrorMessage("Invalid move");
                    if (session.isOpen()) {
                        session.getRemote().sendString(gson.toJson(newError));
                    }
                }
            } else {
                ErrorMessage newError = new ErrorMessage("Bad input");
                if (session.isOpen()) {
                    session.getRemote().sendString(gson.toJson(newError));
                }
            }
        } catch (InvalidMoveException e) {
            Gson gson = new Gson();
            ErrorMessage newError = new ErrorMessage("Invalid move");
            if (session.isOpen()) {
                session.getRemote().sendString(gson.toJson(newError));
            }
        }
    }

    public void Leave(Session session, String authtoken, Integer gameID) throws IOException, DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        AuthDAO authDAO = new AuthDAO(connection);
        GameDAO gameDAO = new GameDAO(connection);
        Game myGame = gameDAO.ReadGame(gameID);
        Authtoken myToken = authDAO.ReadAuthtoken(authtoken);
        Gson gson = new Gson();
        if (myGame != null && myToken != null) {
            String rootClient = myToken.getUsername();
            // this step removes the rootClient from the game
            connections.remove(rootClient, myGame.getGameID());
            gameDAO.updateGame(myGame);
            String notification = String.format("%s left the game", rootClient);
            Notification newNotification = new Notification(notification);
            connections.broadcast(gameID, gson.toJson(newNotification), "");
        } else {
            ErrorMessage newError = new ErrorMessage("Bad input");
            session.getRemote().sendString(gson.toJson(newError));
        }
    }

    public void Resign(Session session, String authtoken, Integer gameID) throws IOException, DataAccessException {
        Database database = new Database();
        Connection connection = database.getConnection();
        AuthDAO authDAO = new AuthDAO(connection);
        GameDAO gameDAO = new GameDAO(connection);
        Game myGame = gameDAO.ReadGame(gameID);
        Authtoken myToken = authDAO.ReadAuthtoken(authtoken);
        Gson gson = new Gson();
        String rootClient = myToken.getUsername();

        // check to make sure observer can't resign and it's not the end of the game
        if (myGame != null && myToken != null && !myGame.getGame().isMarkEndOfGame() &&
                ((myGame.getBlackUsername().equals(rootClient)) || (Objects.equals(myGame.getWhiteUsername(), rootClient)))) {

            myGame.getGame().setMarkEndOfGame(true);
            gameDAO.updateGame(myGame);
            String notification = String.format("%s resigned and left the game", rootClient);
            Notification newNotification = new Notification(notification);
            connections.broadcast(gameID, gson.toJson(newNotification), "");
        } else {
            ErrorMessage newError = new ErrorMessage("Bad input");
            session.getRemote().sendString(gson.toJson(newError));
        }
    }

    private boolean isPlayerTurn(Authtoken authtoken, Game myGame) {
        //check to make sure it is the right color player's turn
        ChessGame chessGame = myGame.getGame();
        ChessGame.TeamColor playerColor = chessGame.getTeamTurn();
        return (authtoken.getUsername().equals(myGame.getWhiteUsername()) && playerColor == ChessGame.TeamColor.WHITE)
                || (authtoken.getUsername().equals(myGame.getBlackUsername()) && playerColor == ChessGame.TeamColor.BLACK);
    }


    public boolean isValidMove(ChessMove moves, Game game) {
        // check to make sure it's a valid move
        Collection<ChessMove> finalMoves = game.getGame().validMoves(moves.getStartPosition());
        return !finalMoves.isEmpty() && finalMoves.contains(moves);
    }

    static class moveAdapter implements JsonDeserializer<ChessMove> {
        public ChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return new Gson().fromJson(el, Move.class);
        }
    }

    static class posAdapter implements JsonDeserializer<ChessPosition> {
        public ChessPosition deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return new Gson().fromJson(el, Position.class);
        }
    }
}