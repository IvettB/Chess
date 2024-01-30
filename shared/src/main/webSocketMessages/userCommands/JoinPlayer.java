package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private Integer gameID;
    private ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = teamColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getTeamColor() {
        return playerColor;
    }
}
