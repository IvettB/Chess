package Server;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Connection>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session, String authtoken, String username, ChessGame.TeamColor playerColor) {
        connections.compute(gameID, (key, existingSet) -> {
            if (existingSet == null) {
                existingSet = ConcurrentHashMap.newKeySet();
            }
            existingSet.add(new Connection(username, session, gameID, authtoken, playerColor));
            return existingSet;
        });
    }

    public void remove(String username, Integer gameID) {
        Set<Connection> connectionSet = connections.get(gameID);
        for (var c : connectionSet) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    connectionSet.remove(c);
                    c.session.close();
                }
            }
        }
    }

    public void broadcast(Integer gameID, String notification, String excludeUsername) throws IOException {
        List<Connection> closedConnection = new ArrayList();
        Set<Connection> connectionSet = connections.get(gameID);
        for (var c : connectionSet) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(notification);
                    //System.out.print("In the notification message");
                }
            } else {
                closedConnection.add(c);
            }
        }
        closedConnection.forEach(connectionSet::remove);
    }
}