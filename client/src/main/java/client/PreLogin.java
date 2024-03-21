package client;

import dataAccess.DataAccessException;

import java.util.Arrays;

public class PreLogin {
    public String url;
    public String help() {
                          return """
                                  register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                                  login <USERNAME> <PASSWORD> - to play chess
                                  quit - playing chess
                                  help - with possible commands
                                  """;
    }
    public Object eval(String input, String url) {
        url = url;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(parameters);
                case "login" -> login(parameters);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }
    public String register(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            server.register(parameters);
            return "You have registered successfully!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String login(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            server.login(parameters);
            return "You have login successfully!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
