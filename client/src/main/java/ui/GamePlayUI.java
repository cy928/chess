package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccessError.DataAccessException;


import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GamePlayUI {
    static webSocket.WebSocketFacade server;
    public static ChessGame chessGame = new ChessGame();
    public static BoardDrawing drawing = new BoardDrawing();
    static Integer gameId;
    public static ChessGame.TeamColor teamColor;
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
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
        server.leave(gameId, teamColor);
        Repl.state = State.POSTLOGIN;
        return "You have left successfully!";
    }
    public String makeMove(String[] parameters) throws DataAccessException, ResponseException {
        String[] start=parameters[0].split("");
        ChessPosition startPosition=new ChessPosition(parseInt(start[0]), parseInt(start[1]));
        String[] end=parameters[1].split("");
        ChessPosition endPosition=new ChessPosition(parseInt(end[0]), parseInt(end[1]));
        ChessMove move=new ChessMove(startPosition, endPosition, null);
        try {
            chessGame.makeMove(move);
        } catch (InvalidMoveException e) {
            return e.getMessage();
        }
        server.makeMove(gameId, move);
        return redrawChessBoard();
    }
    public String resign() throws DataAccessException, ResponseException {
        while (true) {
            System.out.println("Are you sure to resign from the game? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("no")) {
                return "You are back to the game!";
            } else if (line.equalsIgnoreCase("yes")) {
                server.resign(gameId);
                Repl.state = State.POSTLOGIN;
                return "You have resigned from the game!";
            }
        }
    }
    public String highlightLegalMoves(String[] parameters) throws DataAccessException, ResponseException {
        String[] currentSpot = parameters[0].toLowerCase().split("");
        Collection<ChessMove> moves = chessGame.validMoves(new ChessPosition(parseInt(currentSpot[0]), parseInt(currentSpot[1])));
        if (teamColor == ChessGame.TeamColor.BLACK) {
            drawing.highlightValid(BoardDrawing.board, ChessGame.TeamColor.BLACK, new ChessPosition(parseInt(currentSpot[0]), parseInt(currentSpot[1])), moves);
        } else {
            drawing.highlightValid(BoardDrawing.board, ChessGame.TeamColor.WHITE, new ChessPosition(parseInt(currentSpot[0]), parseInt(currentSpot[1])), moves);
        }
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
