package client;

import dataAccess.DataAccessException;
import result.Game;
import result.ListGameResult;
import ui.Board;

import java.util.Arrays;

import static ui.Board.board;

public class PostLogin {
    public String url;
    ServerFacade server = new ServerFacade(Repl.url);

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
                case "observe" -> observe(parameters);
                case "logout" -> logout();
                case "clear" -> clear();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }
    public String create(String[] parameters) throws DataAccessException {
        try {
            server.create(parameters);
            return "You have created a game!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String list() throws DataAccessException {
        String output="\n";
        try {
            ListGameResult response = server.list();
            for (Game game : response.games()){
                output += ("Name: "+ game.gameName() +" ID: " + game.gameID()+ " WhitePlayer: " + game.whiteUsername() + " BlackPlayer: " + game.blackUsername());
                output +="\n";
            }
            return output;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String join(String[] parameters) throws DataAccessException {
        try {
            server.join(parameters);
            Board board = new Board();
            board.main(parameters);
            return "You have joined a game!";
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public String observe(String[] parameters) throws DataAccessException {
        Board board = new Board();
        board.main(parameters);
        return "You are now observing the game!";
    }
    public String logout() throws DataAccessException {
        try {
            server.logout();
            Repl.state = State.PRELOGIN;
            return "You have logout successfully!";
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }
    public String clear() throws DataAccessException {
        try {
            server.clear();
            return "clear DB";
        } catch (DataAccessException ex) {
            throw ex;
        }
    }
}
