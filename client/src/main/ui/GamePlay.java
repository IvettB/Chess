package ui;


import ServerFacade.ServerFacade;
import chess.*;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;

import java.util.Scanner;

import ServerFacade.WebSocketFacade;
import webSocketMessages.userCommands.Resign;

public class GamePlay {
    private final String authtoken = ServerFacade.getAuth();
    private final Integer gameID = PostLogin.getGameID();
    private WebSocketFacade webSocketFacade = null;

    ServerFacade serverFacade = new ServerFacade("http://localhost:8080/");

    public GamePlay(ChessGame.TeamColor teamColor) {
    }

    public GamePlay() {
    }

    public boolean run() throws Exception {
        webSocketFacade = new WebSocketFacade();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[IN GAME] >>> ");
            String command = scanner.next();

            switch (command) {
                case "help" -> {
                    HelpUser();
                }
                case "redraw" -> {
                    RedrawBoard();
                }
                case "leave" -> {
                    Leave(authtoken, gameID);
                    // send the client back to post login
                    PostLogin login = new PostLogin(serverFacade);
                    login.run();
                }
                case "move" -> {

                    System.out.print("Start position: ");
                    Scanner scanner1 = new Scanner(System.in);
                    String start = scanner1.next();
                    // divide the input string up to two ints
                    // first, get the row number
                    int startRow = Character.getNumericValue(start.charAt(1));
                    // second, get the character
                    int startCol = start.charAt(0) - 'a';
                    ChessPosition startPosition = new Position(startRow, startCol);

                    System.out.print("End position: ");
                    Scanner scanner2 = new Scanner(System.in);
                    String end = scanner2.next();
                    int endRow = Character.getNumericValue(end.charAt(1));
                    int endCol = end.charAt(0) - 'a';
                    ChessPosition endPosition = new Position(endRow, endCol);


                    MakeGameMove(authtoken, gameID, new Move(startPosition, endPosition, null));
                }
                case "resign" -> {
                    Resign(authtoken, gameID);
                    // send client back to post login
                    PostLogin login = new PostLogin(serverFacade);
                    login.run();
                }
                case "highlight" -> {
                    HighlightMoves();
                }
                default -> {
                    System.out.println("Unknown command. Please use one of the following commands:");
                    HelpUser();
                }
            }
        }
    }

    private void HelpUser() {
        System.out.println("help - with possible commands");
        System.out.println("redraw - redraws the current chessboard");
        System.out.println("leave - leave the game");
        System.out.println("make move - to make a chessmove");
        System.out.println("resign - to resign from the current chessgame");
        System.out.println("highlight - all possible valid moves");
    }

    private void RedrawBoard() {
        //not implemented
    }

    private void Leave(String authtoken, Integer gameID) throws Exception {
        Leave leave = new Leave(authtoken, gameID);
        WebSocketFacade webSocketFacade = new WebSocketFacade();
        webSocketFacade.Leave(leave);
    }

    private void MakeGameMove(String authtoken, Integer gameID, ChessMove move) throws Exception {
        MakeMove makeMove = new MakeMove(authtoken, gameID, move);
        WebSocketFacade webSocketFacade = new WebSocketFacade();
        webSocketFacade.MakeMove(makeMove);
    }

    private void Resign(String authtoken, Integer gameID) throws Exception {
        Resign resign = new Resign(authtoken, gameID);
        WebSocketFacade webSocketFacade = new WebSocketFacade();
        webSocketFacade.Resign(resign);
    }

    private void HighlightMoves() {
        // not implemented
    }
}