package client;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.*;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;
import result.UserResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Integer.parseInt;

public class ServerFacade {
    public String serverURL;
    private String authToken;

    public ServerFacade(String url) {
        serverURL = url;
    }
    public void register(String[] parameters) throws DataAccessException {
        var path="/user";
        RegisterRequest request = new RegisterRequest(parameters[0], parameters[1], parameters[2]);
        this.makeRequest("POST", path, request, UserResult.class);
    }

    public void login(String[] parameters) throws DataAccessException {
        var path="/session";
        LoginRequest request = new LoginRequest(parameters[0], parameters[1]);
        this.makeRequest("POST", path, request, UserResult.class);
    }

    public void create(String[] parameters) throws DataAccessException {
        var path="/game";
        CreateGameRequest request = new CreateGameRequest(parameters[0]);
        this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public void list() throws DataAccessException {
        var path="/game";
        this.makeRequest("GET", path, null, ListGameResult.class);
    }

    public void join(String[] parameters) throws DataAccessException {
        var path="/game";
        JoinGameRequest request = new JoinGameRequest(parameters[0], parseInt(parameters[1]));
        this.makeRequest("PUT", path, request, Game.class);
    }

    public void logout() throws DataAccessException {
        var path="/session";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URI uri=new URI(Repl.url + path);
            HttpURLConnection http=(HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            writeRequestBody(request, http);
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            return readResponseBody(http, responseClass);
        } catch (URISyntaxException|IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    private static void writeRequestBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.setDoOutput(true);
            var body = new Gson().toJson(request);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }
    private static <T>T readResponseBody(HttpURLConnection http, Class<T> responseClass) throws DataAccessException {
        T responseBody=null;
        try {
            if (http.getResponseCode() == 200) {
                InputStream respBody=http.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, responseClass);
                return responseBody;
            } else {
                InputStream respBody=http.getErrorStream();
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                var response = new Gson().fromJson(inputStreamReader, DataAccessException.class);
                throw response;
            }
        } catch (IOException e) {
          throw new DataAccessException(e.getMessage());
        }
    }
}
