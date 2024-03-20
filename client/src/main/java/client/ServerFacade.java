package client;

import dataAccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;
import result.UserResult;

import static java.lang.Integer.parseInt;

public class ServerFacade {
    public String serverURL;

    public ServerFacade(String url) {
        serverURL = url;
    }
    public UserResult register(String[] parameters) throws DataAccessException {
        var path="/user";
        RegisterRequest request = new RegisterRequest(parameters[0], parameters[1], parameters[2]);
        return this.makeRequest("POST", path, request, UserResult.class);
    }

    public UserResult login(String[] parameters) throws DataAccessException {
        var path="/session";
        LoginRequest request = new LoginRequest(parameters[0], parameters[1]);
        return this.makeRequest("POST", path, request, UserResult.class);
    }

    public CreateGameResult create(String[] parameters) throws DataAccessException {
        var path="/game";
        CreateGameRequest request = new CreateGameRequest(parameters[0]);
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public ListGameResult list() throws DataAccessException {
        var path="/game";
        return this.makeRequest("POST", path, null, ListGameResult.class);
    }

    public Game join(String[] parameters) throws DataAccessException {
        var path="/game";
        JoinGameRequest request = new JoinGameRequest(parameters[0], parseInt(parameters[1]));
        return this.makeRequest("POST", path, request, Game.class);
    }

    public Object logout() throws DataAccessException {
        var path="/session";
        return this.makeRequest("POST", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {

    }
}
