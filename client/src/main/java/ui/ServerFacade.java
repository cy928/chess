package ui;

import com.google.gson.Gson;
import dataAccessError.DataAccessException;
import request.*;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;
import result.UserResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Integer.parseInt;

public class ServerFacade {
    public String serverURL;
    static String authToken;
    public ServerFacade(String url) {
        serverURL=url;
    }

    public UserResult register(String[] parameters) throws DataAccessException, ResponseException {
        String path="/user";
        RegisterRequest request=new RegisterRequest(parameters[0], parameters[1], parameters[2]);
        UserResult resp=this.makeRequest("POST", path, request, UserResult.class);
        authToken=resp.authToken();
        return resp;
    }

    public UserResult login(String[] parameters) throws DataAccessException, ResponseException {
        String path="/session";
        LoginRequest request=new LoginRequest(parameters[0], parameters[1]);
        UserResult resp=this.makeRequest("POST", path, request, UserResult.class);
        authToken=resp.authToken();
        return resp;
    }

    public void logout() throws DataAccessException, ResponseException {
        String path="/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public CreateGameResult create(String[] parameters) throws DataAccessException, ResponseException {
        String path="/game";
        CreateGameRequest request=new CreateGameRequest(parameters[0]);
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public ListGameResult list() throws DataAccessException, ResponseException {
        String path="/game";
        return this.makeRequest("GET", path, null, ListGameResult.class);
    }

    public void join(String[] parameters) throws DataAccessException, ResponseException {
        String path="/game";
        JoinGameRequest request=new JoinGameRequest(parameters[1], parseInt(parameters[0]));
        this.makeRequest("PUT", path, request, Game.class);
    }

    public void clear() throws DataAccessException, ResponseException {
        String path="/db";
        this.makeRequest("DELETE", path, null, null);
    }
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException, DataAccessException {
        try {
            URI uri=new URI(serverURL + path);
            System.out.println(uri);
            HttpURLConnection http=(HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch(IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
