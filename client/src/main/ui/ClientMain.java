package ui;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        var serverURL = "http://localhost:8080";
        if (args.length == 1) {
            serverURL = args[0];
        }
        new PreLogin(serverURL).run();
    }
}