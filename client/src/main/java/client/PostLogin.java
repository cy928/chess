package client;

import dataAccess.DataAccessException;

import java.util.Arrays;

public class PostLogin {
    public String help() {
        String helpText = "create <NAME> - a game" +
                "list - games" +
                "join <ID> [WHITE|BLACK<empty>] - a game" +
                "observe <ID> - a game" +
                "logout - when you are done" +
                "quit - playing chess" +
                "help - with possible command";
        return helpText;
    }
    public String eval(String input, String url) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(parameters);
                case "list" -> list(parameters);
                case "join" -> join(parameters);
                case "observe" -> observe(parameters);
                case "logout" -> logout(parameters);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }
}
