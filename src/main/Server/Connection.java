package Server;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public Integer gameID;
    public String authtoken;
    public ChessGame.TeamColor playerColor;

    public Connection(String username, Session session, Integer gameID, String authtoken, ChessGame.TeamColor playerColor) {
        this.username = username;
        this.session = session;
        this.gameID = gameID;
        this.authtoken = authtoken;
        this.playerColor = playerColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}