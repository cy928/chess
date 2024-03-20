package client;

import dataAccess.DataAccessException;

import java.util.Arrays;

public class PostLogin {
    public String url;
    public String help() {
        String helpText = "create <NAME> - a game" +
                "list - games" +
                "join <ID> [WHITE|BLACK|<empty>] - a game" +
                "observe <ID> - a game" +
                "logout - when you are done" +
                "quit - playing chess" +
                "help - with possible commands";
        return helpText;
    }
    public Object eval(String input, String url) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(parameters);
                case "list" -> list(parameters);
                case "join" -> join(parameters);
                case "observe" -> observe(parameters);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }
    public Object create(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            return server.create(parameters);
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public Object list(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            return server.list();
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public Object join(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            return server.join(parameters);
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String observe(String[] parameters) throws DataAccessException {
        return "";
    }
    public Object logout() throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            return server.logout();
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
