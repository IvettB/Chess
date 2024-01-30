package Server;

import Handler.JoinGameHandler;
import Handler.RegisterHandler;
import Handler.ClearHandler;
import Handler.LoginHandler;
import Handler.LogoutHandler;
import Handler.ListGamesHandler;
import Handler.CreateGameHandler;
import dataAccess.Database;
//import org.eclipse.jetty.websocket.server.WebSocketServerConnection;
import spark.*;

public class Server {

    public static Database database = new Database();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port (8080) that the server should listen on.
        Spark.port(8080);
        Spark.webSocket("/connect", WebSocketHandler.class);
        // Specify the location of static web files (e.g., HTML, CSS) for serving client-side content.
        Spark.externalStaticFileLocation("web");
        // Initialize Spark, the web framework used for handling HTTP requests.
        Spark.init();

        // Define and configure various HTTP endpoints with corresponding request handlers.
        Spark.delete("/db", (request, response) -> new ClearHandler().handle(response));
        Spark.post("/user", (request, response) -> new RegisterHandler().handle(request, response));
        Spark.post("/session", (request, response) -> new LoginHandler().handle(request, response));
        Spark.delete("/session", (request, response) -> new LogoutHandler().handle(request, response));
        Spark.get("/game", (request, response) -> new ListGamesHandler().handle(request, response));
        Spark.post("/game", (request, response) -> new CreateGameHandler().handle(request, response));
        Spark.put("/game", (request, response) -> new JoinGameHandler().handle(request, response));
    }
}