package ui;

import Model.Game;
import Request.CreateGameRequest;
import Request.JoinGameRequest;
import Request.ListGamesRequest;
import Request.LogoutRequest;
import Response.*;
import ServerFacade.ServerFacade;
import ServerFacade.WebSocketFacade;
import chess.ChessGame;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;

import java.util.HashMap;
import java.util.Scanner;

public class PostLogin {

    HashMap<Integer, Integer> myMap;
    private ServerFacade url;
    private static Integer gameID;

    public static Integer getGameID() {
        return gameID;
    }

    public PostLogin(ServerFacade serverURL) {
        url = serverURL;
        myMap = new HashMap<>();
    }

    public boolean run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[LOGGED IN] >>> ");

            String[] command = scanner.nextLine().split(" "); // or .nextLine()

            switch (command[0]) {
                case "logout" -> {
                    LogoutUser(new LogoutRequest(null));
                    return true;
                }
                case "help" -> {
                    HelpUser();
                }
                case "quit" -> {
                    return false;
                }
                case "create" -> {
                    if (command.length == 2) {
                        CreateGame(new CreateGameRequest(command[1], null));
                    } else {
                        System.out.println("Wrong input, please start over");
                    }
                }
                case "join" -> {
                    if (command.length == 3 && command[1] != null) {
                        JoinGame(new JoinGameRequest(command[2], myMap.get(Integer.parseInt(command[1])), null)); //map: always returns the second value of the pair
                    } else {
                        System.out.println("Wrong input, please try again");
                    }
                }
                case "observe" -> {
                    if (command.length == 2 && command[1] != null) {
                        ObserveGame(new JoinGameRequest(null, myMap.get(Integer.parseInt(command[1])), null));
                    } else {
                        System.out.println("Wrong input, please try again");
                    }
                }
                case "list" -> {
                    ListGames(new ListGamesRequest(null));
                }
                default -> {
                    System.out.println("Unknown command. Please use one of the following commands:");
                    HelpUser();
                }
            }
        }
    }


    private void LogoutUser(LogoutRequest request) throws Exception {
        try {
            LogoutResponse response = url.Logout(request);
            if (response.getMessage() == null) {
                System.out.println("Logout successfull");
                PreLogin logout = new PreLogin("http://localhost:8080");
                logout.run();
            } else {
                System.out.println("Logout failed");
            }
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
        }
    }

    private void HelpUser() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
        System.out.println("logout - when you are done");
        System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
        System.out.println("observe <ID> - a game");
    }

    private void CreateGame(CreateGameRequest request) throws Exception {
        try {
            CreateGameResponse response = url.CreateGame(request);

            if (response.getMessage() == null) {
                int gameID = response.getGameID();
                String message = "Game created successfully\n";
                System.out.println(message + "You can join game: " + request.getGameName() + "\nwith Game ID: " + gameID);
            }
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
        }
    }

    private void JoinGame(JoinGameRequest request) throws Exception {
        try {
            JoinGameResponse response = url.JoinGame(request);

            if (response.getMessage() == null) {
                gameID = request.getGameID();
                System.out.flush();
                System.out.println("You successfully joined the game");
                // default to white, and change to black if input is black
                ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
                if (request.getPlayerColor().equals("BLACK")) {
                    color = ChessGame.TeamColor.BLACK;
                }
                JoinPlayer join = new JoinPlayer(request.getAuthtoken(), request.getGameID(), color);
                WebSocketFacade facade = new WebSocketFacade();
                facade.JoinPlayer(join);
                GamePlay gamePlay = new GamePlay(color);
                try {
                    // the player transitions to the Game UI
                    gamePlay.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Game join was NOT successful");
                System.out.println(response.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
        }
    }

    private void ObserveGame(JoinGameRequest request) throws Exception {
        try {
            JoinGameResponse response = url.JoinGame(request);

            if (response.getMessage() == null) {
                System.out.flush();
                System.out.println("\nYou successfully joined as an observer\n");
                JoinObserver observe = new JoinObserver(request.getAuthtoken(), request.getGameID());
                WebSocketFacade facade = new WebSocketFacade();
                facade.JoinObserver(observe);
                GamePlay gamePlay = new GamePlay();
                try {
                    gamePlay.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Game join as observer was NOT successful");
                System.out.println(response.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
        }
    }

    private void ListGames(ListGamesRequest request) throws Exception {
        try {
            ListGamesRequest request1 = new ListGamesRequest(request.getAuthtoken());
            ListGamesResponse response = url.ListGames(request1);
            int index = 1;
            Game[] games = response.getGame();

            if (response.getMessage() == null && games.length != 0) {
                StringBuilder stringBuilder = new StringBuilder("Here are the available games:\n");

                for (Game game : games) {
                    stringBuilder.append(String.format("ID: %5d    Game ID: %10d    Game Name: %10s    White User: %10s    Black User: %10s\n", index, game.getGameID(), game.getGameName(), game.getWhiteUsername(), game.getBlackUsername()));
                    myMap.put(index++, game.getGameID());
                }
                System.out.println(stringBuilder);
            } else {
                System.out.println("You have no games available");
            }
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
        }
    }
}