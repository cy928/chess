package client;

import dataAccess.DataAccessException;
import result.UserResult;

import java.util.Arrays;

public class PreLogin {
    public String url;
    public String help() {
        String helpText = "register <USERNAME> <PASSWORD> <EMAIL> - to create an account" +
                          "login <USERNAME> <PASSWORD> - to play chess" +
                          "quit - playing chess" +
                          "help - with possible commands";
        return helpText;
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
    public UserResult register(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            var result = server.register(parameters);
            return result;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public UserResult login(String[] parameters) throws DataAccessException {
        try {
            ServerFacade server = new ServerFacade(url);
            var result = server.login(parameters);
            return result;
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
