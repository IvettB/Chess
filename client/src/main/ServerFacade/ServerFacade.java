package ServerFacade;

import Model.Authtoken;
import Request.*;
import Response.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private static String auth;

    private final String serverUrl;

    private static Integer gameID;

    public static String getAuth() {
        return auth;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RegisterResponse RegisterUser(RegisterRequest request) throws Exception {
        var path = "/user";
        RegisterResponse response = this.makeRequest("POST", path, request, RegisterResponse.class);
        auth = response.getAuthToken();
        return response;
    }

    public LoginResponse Login(LoginRequest request) throws Exception {
        var path = "/session";
        LoginResponse response = this.makeRequest("POST", path, request, LoginResponse.class);
        auth = response.getAuthToken();
        return response;
    }

    public LogoutResponse Logout(LogoutRequest request) throws Exception {
        var path = "/session";
        if (request.getAuthtoken() != null) {
            auth = request.getAuthtoken();
        }
        return this.makeRequest("DELETE", path, request, LogoutResponse.class);
    }

    public ClearResponse Clear() throws Exception {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, ClearResponse.class);
    }

    public ListGamesResponse ListGames(ListGamesRequest request) throws Exception {
        var path = "/game";
        if (request.getAuthtoken() != null) {
            auth = request.getAuthtoken();
        }
        return this.makeRequest("GET", path, null, ListGamesResponse.class);
    }

    public JoinGameResponse JoinGame(JoinGameRequest request) throws Exception {
        var path = "/game";
        if (request.getAuthtoken() != null) {
            auth = request.getAuthtoken();
        } else {
            request.setAuthtoken(auth);
        }
//        if (request.getAuthtoken() == null) {
//            request.setAuthtoken(auth);
//        } else {
//            auth = request.getAuthtoken();
//        }
        return this.makeRequest("PUT", path, request, JoinGameResponse.class);
    }

    public CreateGameResponse CreateGame(CreateGameRequest request) throws Exception {
        var path = "/game";
        if (request.getAuthtoken() != null) {
            auth = request.getAuthtoken();
        }
        return this.makeRequest("POST", path, request, CreateGameResponse.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = new URI(serverUrl + path).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            if (auth != null) {
                //write header
                http.addRequestProperty("Authorization", auth);
            }

            if (request != null) {
                if (method.equals("GET")) {
                    throw new Exception("Gets cant' have bodies");
                }
                http.setDoOutput(true);
                writeBody(request, http);
            }
            http.connect();
            //throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Connect-Type", "application");
            String reqData = new Gson().toJson(request);

            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;

        if (http.getContentLength() < 0) {
            if (isSuccessful(http.getResponseCode())) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    if (responseClass != null) {
                        response = new Gson().fromJson(reader, responseClass);
                    }
                }
            } else {
                try (InputStream respBody = http.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    if (responseClass != null) {
                        response = new Gson().fromJson(reader, responseClass);
                    }
                }
            }
        }
        return response;
    }

//    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
//        var status = http.getResponseCode();
//        if (!isSuccessful(status)) {
//            throw new Exception();
//        }
//    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}