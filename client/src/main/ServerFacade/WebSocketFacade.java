package ServerFacade;


import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import ui.ChessBoardDrawer;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;
import java.util.Objects;

public class WebSocketFacade extends Endpoint {
    public Session session;

    public WebSocketFacade() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            try {
                var deserializer = createGsonDeserializer();
                ServerMessage serverMessage = deserializer.fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case ERROR -> ErrorNotification(message);
                    case LOAD_GAME -> LoadGameNotification(message);
                    case NOTIFICATION -> RegularNotification(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    private void ErrorNotification(String message) {
        Gson gson = new Gson();
        ErrorMessage error = gson.fromJson(message, ErrorMessage.class);
        String errorMessage = error.getErrorMessage();
        // make sure the error message contains the word "error'
        System.out.print("ERROR: " + errorMessage);
    }

    private void LoadGameNotification(String message) {
        var deserializer = createGsonDeserializer();
        LoadGame loadGame = deserializer.fromJson(message, LoadGame.class);
        System.out.println("\n");
        ChessBoardDrawer boardDrawer = new ChessBoardDrawer();
        // drawing the white board
        if (Objects.equals(loadGame.getGame().getWhiteUsername(), loadGame.getUsername())) {
            boardDrawer.drawBoard(loadGame.getGame().getGameID(), "WHITE", loadGame.getGame().getGame());
        } else {
            // drawing the black board
            boardDrawer.drawBoard(loadGame.getGame().getGameID(), "BLACK", loadGame.getGame().getGame());
        }
        System.out.println("\n");
    }

    private void RegularNotification(String message) {
        Gson gson = new Gson();
        Notification notification = gson.fromJson(message, Notification.class);
        String notificationMessage = notification.getMessage();
        System.out.print(notificationMessage);
    }

    public void JoinObserver(JoinObserver observer) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(observer));
    }

    public void JoinPlayer(JoinPlayer player) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(player));
    }

    public void Leave(Leave leave) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(leave));
    }

    public void MakeMove(MakeMove move) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(move));
    }

    public void Resign(Resign resign) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(resign));
    }

    public static Gson createGsonDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, Game.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, Board.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, Piece.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, Move.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, Position.class));

        gsonBuilder.registerTypeAdapter(Piece.class,
                (JsonDeserializer<Piece>) (el, type, ctx) -> {
                    Piece chessPiece = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("type").getAsString();
                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
                            case PAWN -> chessPiece = ctx.deserialize(el, Pawn.class);
                            case ROOK -> chessPiece = ctx.deserialize(el, Rook.class);
                            case KNIGHT -> chessPiece = ctx.deserialize(el, Knight.class);
                            case BISHOP -> chessPiece = ctx.deserialize(el, Bishop.class);
                            case QUEEN -> chessPiece = ctx.deserialize(el, Queen.class);
                            case KING -> chessPiece = ctx.deserialize(el, King.class);
                        }
                    }
                    return chessPiece;
                });
        return gsonBuilder.create();
    }
}