package ui;

import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Response.RegisterResponse;
import ServerFacade.ServerFacade;

import java.util.Scanner;

public class PreLogin {
    private ServerFacade url;

    public PreLogin(String serverURL) {
        url = new ServerFacade(serverURL);
    }

    public boolean run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to CS 240 Chess! Type help to get started");
        while (true) {
            System.out.print("[LOGGED OUT] >>> ");
            String command = scanner.next(); // or .nextLine()

            switch (command) {
                case "login" -> {
                    LoginUser();
                    return true;
                }
                case "help" -> {
                    HelpUser();
                }
                case "quit" -> {
                    return false;
                }
                case "register" -> {
                    RegisterUser();
                    return true;
                }
                default -> {
                    System.out.println("Unknown command. Please use one of the following commands:");
                    HelpUser();
                }
            }
        }
    }

    private void LoginUser() {
        System.out.println("Username: ");
        Scanner scanner1 = new Scanner(System.in);
        String username = scanner1.next();

        System.out.println("Password: ");
        Scanner scanner2 = new Scanner(System.in);
        String password = scanner2.next();

        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = null;
        try {
            response = url.Login(request);
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
            return;
        }

        if (response.getMessage() == null) {
            PostLogin login = new PostLogin(url);
            System.out.println("Logged in as " + username);
            try {
                login.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //source of problems possibly?
        else {
            System.out.println("Wrong username or password, please start over or register");
            //System.out.println(response.getMessage());

        }
    }

    private void RegisterUser() {
        System.out.println("Username: ");
        Scanner scanner1 = new Scanner(System.in);
        String username = scanner1.next();

        System.out.println("Password: ");
        Scanner scanner2 = new Scanner(System.in);
        String password = scanner2.next();

        System.out.println("Email: ");
        Scanner scanner3 = new Scanner(System.in);
        String email = scanner3.next();

        RegisterRequest request = new RegisterRequest(username, password, email);
        RegisterResponse response = null;
        try {
            response = url.RegisterUser(request);
        } catch (Exception e) {
            System.out.println("Sorry, something went wrong");
            e.printStackTrace();
            return;
        }

        if (response.getMessage() == null) {
            PostLogin login = new PostLogin(url);
            System.out.println("Logged in as " + username);
            try {
                login.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //source of problems possibly?
        else {
            System.out.println("Wrong request, please start over");
            RegisterUser();
        }
    }

    private void HelpUser() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }
}