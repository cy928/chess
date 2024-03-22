package ui;

import dataAccess.DataAccessException;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;

import java.util.Arrays;

public class PostLogin {
    ServerFacade server = new ServerFacade(Repl.serverURL);
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> create(parameters);
                case "list" -> list();
                case "observe" -> observe(parameters);
                case "join" -> join(parameters);
                case "clearDatabase" -> clearDatabase();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException | ResponseException e) {
            return e.getMessage();
        }
    }
    public String logout() throws DataAccessException, ResponseException {
        server.logout();
        Repl.state = State.PRELOGIN;
        return "You have logout successfully!";
    }
    public String create(String[] parameters) throws DataAccessException, ResponseException {
        CreateGameResult resp =server.create(parameters);
        return "You have created a game with game ID: " + resp.gameID() + ".";
    }
    public String list() throws DataAccessException, ResponseException {
        StringBuilder output=new StringBuilder("\n");
        ListGameResult response = server.list();
        for (Game game : response.games()){
            output.append("Name: ").append(game.gameName()).append(" ID: ").append(game.gameID()).append(" WhitePlayer: ").append(game.whiteUsername()).append(" BlackPlayer: ").append(game.blackUsername());
            output.append("\n");
        }
        return output.toString();
    }
    public String observe(String[] parameters) throws DataAccessException {
        BoardDrawing.main(parameters);
        return "You are now observing the game!";
    }
    public String join(String[] parameters) throws DataAccessException, ResponseException {
        server.join(parameters);
        BoardDrawing.main(parameters);
        return "You have joined a game!";
    }
    public String clearDatabase() throws DataAccessException, ResponseException {
        server.clear();
        return "clear DB";
    }
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
}
