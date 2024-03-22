package client;

import dataAccess.DataAccessException;

import java.util.Arrays;

public class PostLogin {
    public String url;
    public String help() {
          return """
                  create <NAME> - a game
                  list - games
                  join <ID> [WHITE|BLACK|<empty>] - a game
                  observe <ID> - a game
                  logout - when you are done
                  quit - playing chess
                  help - with possible commands
                  """;
    }
    public String eval(String input, String url) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(parameters);
                case "list" -> list();
                case "join" -> join(parameters);
                case "observe" -> observe();
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }
    public String create(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            server.create(parameters);
            return "You have created a game!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String list() throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            server.list();
            return "This is the list of games!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String join(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            server.join(parameters);
            return "You have joined a game!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String observe() throws DataAccessException {
        return "You are now observing the game!";
    }
    public String logout() throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            server.logout();
            Repl.state = State.PRELOGIN;
            return "You have logout successfully!";
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }
}
