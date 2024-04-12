package ui;

import chess.ChessGame;
import dataAccessError.DataAccessException;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;
import webSocket.NotificationHandler;
import webSocket.WebSocketFacade;
import static ui.ServerFacade.authToken;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class PostLogin {
    ServerFacade server = new ServerFacade(Repl.serverURL);
    private final NotificationHandler notificationHandler;

    public PostLogin(NotificationHandler notificationHandler){
        this.notificationHandler = notificationHandler;
    }
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
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
        Integer gameID = parseInt(parameters[0]);
        WebSocketFacade webServer = new WebSocketFacade(Repl.serverURL, this.notificationHandler, authToken);
        webServer.joinObserver(gameID);
        Repl.state = State.GAMEPLAYUI;
        GamePlayUI.teamColor=null;
        GamePlayUI.gameId= gameID;
        GamePlayUI.server= webServer;
        return "You are now observing the game!";
    }
    public String join(String[] parameters) throws DataAccessException, ResponseException {
        server.join(parameters);
        ChessGame.TeamColor color;
        Integer gameID = parseInt(parameters[0]);
        if (parameters[1].equalsIgnoreCase("black")) {
            color = ChessGame.TeamColor.BLACK;
        } else {
            color = ChessGame.TeamColor.WHITE;
        }
        WebSocketFacade webServer = new WebSocketFacade(Repl.serverURL, this.notificationHandler, authToken);
        webServer.joinPlayer(gameID, color);
        Repl.state = State.GAMEPLAYUI;
        GamePlayUI.teamColor= color;
        GamePlayUI.gameId= gameID;
        GamePlayUI.server= webServer;
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
