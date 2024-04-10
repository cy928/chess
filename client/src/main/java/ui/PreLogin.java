package ui;

import dataAccess.DataAccessException;
import result.UserResult;

import java.util.Arrays;

public class PreLogin {
    ServerFacade server = new ServerFacade(Repl.serverURL);
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(parameters);
                case "login" -> login(parameters);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException | ResponseException e) {
            return e.getMessage();
        }
    }
    public String register(String[] parameters) throws DataAccessException, ResponseException {
        UserResult resp=server.register(parameters);
        Repl.state = State.POSTLOGIN;
        return "Hello " + resp.username() + "! You have registered successfully!";
    }
    public String login(String[] parameters) throws DataAccessException, ResponseException {
        UserResult resp=server.login(parameters);
        Repl.state = State.POSTLOGIN;
        return "Hello " + resp.username() + "! You have login successfully!";
    }
    public String help() {
        return """
            register <USERNAME> <PASSWORD> <EMAIL> - to create an account
            login <USERNAME> <PASSWORD> - to play chess
            quit - playing chess
            help - with possible commands
            """;
    }
}
