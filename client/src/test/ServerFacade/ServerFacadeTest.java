package ServerFacade;

import Request.*;
import Response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerFacadeTest {

    @BeforeEach
    public void BeforeEach() throws Exception {
        new ServerFacade("http://localhost:8080").Clear();
    }

    // REGISTER TEST BEGIN //

    @Test
    public void RegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        Assertions.assertNull(new ServerFacade("http://localhost:8080").RegisterUser(request).getMessage());
    }

    @Test
    public void RegisterFail() throws Exception {
        RegisterRequest request = new RegisterRequest("pamelapumpkin", "", "pp@gmail.com");
        new ServerFacade("http://localhost:8080").RegisterUser(request);
        //why? should be "Error: bad request"
        Assertions.assertEquals("Error: already taken", new ServerFacade("http://localhost:8080").RegisterUser(request).getMessage());
    }

    // REGISTER TEST END //

    // LOGIN TEST BEGIN

    @Test
    public void LoginSuccess() throws Exception {
        RegisterRequest Registerrequest = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        new ServerFacade("http://localhost:8080").RegisterUser(Registerrequest);
        LoginRequest request = new LoginRequest("pamelapumpkin", "bummer87");
        Assertions.assertNull(new ServerFacade("http://localhost:8080").Login(request).getMessage());
    }

    @Test
    public void LoginFail() throws Exception {
        RegisterRequest Registerrequest = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        new ServerFacade("http://localhost:8080").RegisterUser(Registerrequest);
        LoginRequest request = new LoginRequest("pamelapumpkin", "");
        //might need to change this
        Assertions.assertEquals("Error: unauthorized", new ServerFacade("http://localhost:8080").Login(request).getMessage());
    }

    // LOGIN TEST END

    // LOGOUT TEST BEGIN

    @Test
    public void LogoutSuccess() throws Exception {
        RegisterRequest Registerrequest = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        RegisterResponse response = new ServerFacade("http://localhost:8080").RegisterUser(Registerrequest);
        LogoutRequest request = new LogoutRequest(response.getAuthToken());
        Assertions.assertNull(new ServerFacade("http://localhost:8080").Logout(request).getMessage());
    }

    @Test
    public void LogoutFail() throws Exception {
        LogoutRequest request = new LogoutRequest("12345");
        Assertions.assertEquals("Error: unauthorized", new ServerFacade("http://localhost:8080").Logout(request).getMessage());
    }

    // LOGOUT TEST END

    // LIST GAMES BEGIN

    @Test
    public void ListGamesSuccess() throws Exception {
        RegisterRequest Registerrequest = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        RegisterResponse Rresponse = new ServerFacade("http://localhost:8080").RegisterUser(Registerrequest);
        CreateGameRequest request1 = new CreateGameRequest("chess1", Rresponse.getAuthToken());
        CreateGameRequest request2 = new CreateGameRequest("chess2", Rresponse.getAuthToken());
        CreateGameRequest request3 = new CreateGameRequest("chess3", Rresponse.getAuthToken());
        CreateGameResponse response1 = new ServerFacade("http://localhost:8080").CreateGame(request1);
        CreateGameResponse response2 = new ServerFacade("http://localhost:8080").CreateGame(request2);
        CreateGameResponse response3 = new ServerFacade("http://localhost:8080").CreateGame(request3);
        ListGamesRequest request = new ListGamesRequest(Rresponse.getAuthToken());
        ListGamesResponse response = new ServerFacade("http://localhost:8080").ListGames(request);
        Assertions.assertNull(response.getMessage());
        Assertions.assertEquals(3, response.getGame().length);
    }

    @Test
    public void ListGamesFail() throws Exception {
        ListGamesRequest request = new ListGamesRequest(null);
        Assertions.assertEquals("Error: unauthorized", new ServerFacade("http://localhost:8080").ListGames(request).getMessage());
    }

    // LIST GAMES END

    // JOIN GAME BEGIN

    @Test
    public void JoinGameSuccess() throws Exception {
        RegisterRequest Registerrequest = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        RegisterResponse Registerresponse = new ServerFacade("http://localhost:8080").RegisterUser(Registerrequest);
        CreateGameRequest request1 = new CreateGameRequest("chess1", Registerresponse.getAuthToken());
        CreateGameResponse response1 = new ServerFacade("http://localhost:8080").CreateGame(request1);
        JoinGameRequest request2 = new JoinGameRequest("WHITE", response1.getGameID(), Registerresponse.getAuthToken());
        JoinGameResponse response2 = new ServerFacade("http://localhost:8080").JoinGame(request2);
        Assertions.assertNull(response2.getMessage());
    }

    @Test
    public void JoinGameFail() throws Exception {
        JoinGameRequest request = new JoinGameRequest("WHITE", 12345, "pam");
        Assertions.assertEquals("Error: bad request", new ServerFacade("http://localhost:8080").JoinGame(request).getMessage());
    }

    // JOIN GAME END

    // CREATE GAME BEGIN

    @Test
    public void CreateGameSuccess() throws Exception {
        RegisterRequest Registerrequest = new RegisterRequest("pamelapumpkin", "bummer87", "pp@gmail.com");
        RegisterResponse Registerresponse = new ServerFacade("http://localhost:8080").RegisterUser(Registerrequest);
        CreateGameRequest request = new CreateGameRequest("chess1", Registerresponse.getAuthToken());
        CreateGameResponse response = new ServerFacade("http://localhost:8080").CreateGame(request);
        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void CreateGameFail() throws Exception {
        CreateGameRequest request = new CreateGameRequest("chess1");
        Assertions.assertEquals("Error: unauthorized", new ServerFacade("http://localhost:8080").CreateGame(request).getMessage());
    }

    // CREATE GAME END

    // CLEAR BEGIN

    @Test
    public void ClearSuccess() throws Exception {
        RegisterRequest request1 = new RegisterRequest("pamelapumpkin", "hello", "pp@gmail.com");
        RegisterResponse response1 = new ServerFacade("http://localhost:8080").RegisterUser(request1);

        CreateGameRequest request2 = new CreateGameRequest("chess2", response1.getAuthToken());
        CreateGameResponse response2 = new ServerFacade("http://localhost:8080").CreateGame(request2);

        new ServerFacade("http://localhost:8080").Clear();

        LoginRequest request3 = new LoginRequest("pamelapumpkin", "hello");
        LoginResponse response3 = new ServerFacade("http://localhost:8080").Login(request3);

        ListGamesRequest request4 = new ListGamesRequest(response1.getAuthToken());
        ListGamesResponse response4 = new ServerFacade("http://localhost:8080").ListGames(request4);

        Assertions.assertNull(response1.getMessage());
        Assertions.assertNull(response2.getMessage());
        Assertions.assertEquals("Error: unauthorized", response3.getMessage());
        Assertions.assertEquals("Error: unauthorized", response4.getMessage());
    }

    // CLEAR END
}
