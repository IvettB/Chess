package webSocketMessages.serverMessages;

import Model.Game;

public class LoadGame extends ServerMessage {
    private Game game;
    private String username;

    public LoadGame(Game game, String username) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Game getGame() {
        return game;
    }
}
