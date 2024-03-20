package client;

import dataAccess.DataAccessException;

import java.util.ArrayList;
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
    public String eval(String input, String url) {
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
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }
    public String register(String[] parameters) throws DataAccessException {

    }
    public String login(String[] parameters) throws DataAccessException {

    }
}
