package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import result.ListGameResult;


import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GamePlayUI {
    websocket.WebSocketFacade server;
    ChessGame game = new ChessGame();
    Integer gameId;
    ChessGame.TeamColor teamColor;
    BoardDrawing draw = new BoardDrawing();
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawChessBoard();
                case "leave" -> leave();
                case "make" -> makeMove(parameters);
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(parameters);
                default -> help();
            };
        } catch (DataAccessException | ResponseException e) {
            return e.getMessage();
        }
    }
    public String redrawChessBoard() throws DataAccessException, ResponseException {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            BoardDrawing.printWholeBoard(BoardDrawing.board, ChessGame.TeamColor.BLACK);
        } else {
            BoardDrawing.printWholeBoard(BoardDrawing.board, ChessGame.TeamColor.WHITE);
        }
        return "You have redraw the chess board!";
    }
    public String leave() throws DataAccessException, ResponseException {
        try {
            ListGameResult response = server.leave(GameId);
            Repl.state = State.POSTLOGIN;
            return "You have left successfully!";
        }
        catch(DataAccessException e){
            throw e;
        }

    }
    public String makeMove(String[] parameters) throws DataAccessException, ResponseException {
        try {
            String[] startPosition = parameters[0].toLowerCase().split("");
            String[] endPosition = parameters[1].toLowerCase().split("");
            try {
                ChessMove move = new ChessMove(new ChessPosition(parseInt(startPosition[0]),parseInt(startPosition[1])),(new ChessPosition(parseInt(endPosition[0]),parseInt(endPosition[1]))),null);
                game.makeMove(move);
            } catch (InvalidMoveException e) {
                return e.getMessage();
            }
            server.makeMove(gameId, startPosition, endPosition);
            return redrawChessBoard();
        }
        catch(DataAccessException | ResponseException ex){
            throw ex;
        }
        return "You have made a move on the board!";
    }
    public String resign() throws DataAccessException, ResponseException {
        while (true) {
            System.out.println("Are you sure to resign from the game? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.toLowerCase() == "no") {
                return "You are back to the game!";
            } else if (line.toLowerCase() == "yes") {
                server.resign(gameId);
                Repl.state = State.POSTLOGIN;
                return "You have resigned from the game!";
            }
        }
    }
    public String highlightLegalMoves(String[] parameters) throws DataAccessException, ResponseException {
        String[] currentSpot = parameters[0].toLowerCase().split("");
        Collection<ChessMove> list = game.validMoves(new ChessPosition(parseInt(currentSpot[0]), parseInt(currentSpot[1])));
        return "These are the legal moves you can make!";
    }
    public String help() {
        return """                            
                redraw - the chess board
                leave - the game
                make - a move
                resign - from the game
                highlight - legal moves
                help - with possible commands
                """;
    }
}
