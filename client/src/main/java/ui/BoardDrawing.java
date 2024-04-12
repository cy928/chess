package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;


public class BoardDrawing {

    public static ChessBoard board = new ChessBoard();
    static
    {
        board.resetBoard();
    }
    private static final String boardBlackColor = SET_BG_COLOR_DARK_GREY;
    private static final String boardWhiteColor = SET_BG_COLOR_WHITE;
    private static final String boardEdgeColor = SET_BG_COLOR_LIGHT_GREY;
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }

    public static void printWholeBoard(ChessBoard board, ChessGame.TeamColor teamColor) {
        System.out.println();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        printHeaders(out, teamColor);
        if (teamColor == ChessGame.TeamColor.BLACK) {
            for (int i=0; i <= 9; i++) {
                if (i > 0 && i < 9) {
                    out.print(boardEdgeColor);
                    out.print(SET_TEXT_COLOR_BLACK);
                    printLetterWithSpaces(out, String.valueOf(i));
                }
                for (int j=7; j >= 0; j--) {
                    printBoard(board, out, i, j);
                }
                printRowNumber(i, out);
            }
        } else {
            for (int i = 9; i >= 0; i--){
                if (i>0 && i<9) {
                    out.print(boardEdgeColor);
                    out.print(SET_TEXT_COLOR_BLACK);
                    printLetterWithSpaces(out, String.valueOf(i));
                }
                for (int j = 0; j <= 7; j++){
                    printBoard(board, out, i, j);
                }
                printRowNumber(i, out);
            }
        }
        printHeaders(out, teamColor);
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printRowNumber(int i, PrintStream out) {
        if(i >0 && i <9) {
            out.print(boardEdgeColor);
            out.print(SET_TEXT_COLOR_BLACK);
            printLetterWithSpaces(out, String.valueOf(i));
            out.print(RESET_BG_COLOR);
            System.out.println();
        }
    }

    private static void printBoard(ChessBoard board, PrintStream out, int i, int j) {
        if (i > 0 && i < 9) {
            ChessPiece currPiece=board.getPiece(new ChessPosition(i, j + 1));
            if ((i + j) % 2 == 0) {
                out.print(boardWhiteColor);
            } else {
                out.print(boardBlackColor);
            }
            if (!(currPiece == null)) {
                printPiece(out, currPiece.getPieceType(), currPiece.getTeamColor());
                out.print(RESET_BG_COLOR);
            } else {
                out.print(EMPTY);
            }
        }
    }

    private static void printHeaders(PrintStream out, ChessGame.TeamColor callColor) {
        String [] headers;
        out.print(boardEdgeColor);
        out.print(EMPTY);
        out.print(SET_TEXT_COLOR_BLACK);
        if(callColor == ChessGame.TeamColor.WHITE) {
            headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        } else {
            headers = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        }
        for (String header: headers) {
            printLetterWithSpaces(out, header);
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printLetterWithSpaces(PrintStream out, String letter) {
        out.print(" ");
        out.print(letter);
        out.print(" ");
    }

    private static void printPiece(PrintStream out, ChessPiece.PieceType pieceType, ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_RED);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
        }
        printPieceIcon(out, pieceType);
    }

    private static void printPieceIcon(PrintStream out, ChessPiece.PieceType pieceType) {
        String pieceString;
        switch (pieceType) {
            case KING -> pieceString = " K ";
            case QUEEN -> pieceString = " Q ";
            case BISHOP -> pieceString = " B ";
            case ROOK -> pieceString = " R ";
            case KNIGHT -> pieceString = " N ";
            case PAWN -> pieceString = " P ";
            default -> pieceString = EMPTY;
        }
        out.print(pieceString);
    }

    public void updateBoard(ChessGame chessGame) {
        board = chessGame.getBoard();
    }

    public void highlightValid(ChessBoard board, ChessGame.TeamColor teamColor, ChessPosition currentPosition, Collection<ChessMove> validMoves) {

        System.out.println();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        printHeaders(out, teamColor);
        if (teamColor == ChessGame.TeamColor.BLACK) {
            for (int i=0; i <= 9; i++) {
                if (i > 0 && i < 9) {
                    out.print(boardEdgeColor);
                    out.print(SET_TEXT_COLOR_BLACK);
                    printLetterWithSpaces(out, String.valueOf(i));
                }
                for (int j=7; j >= 0; j--) {
                    printHighlightBoard(board, currentPosition, validMoves, out, i, j);
                }
                if (i > 0 && i < 9) {
                    out.print(boardEdgeColor);
                    out.print(SET_TEXT_COLOR_BLACK);
                    printLetterWithSpaces(out, String.valueOf(i));
                    out.print(RESET_BG_COLOR);
                    System.out.println();
                }
            }
        } else {
            for (int i = 9; i >= 0; i--){
                if (i>0 && i<9) {
                    out.print(boardEdgeColor);
                    out.print(SET_TEXT_COLOR_BLACK);
                    printLetterWithSpaces(out, String.valueOf(i));
                }
                for (int j = 0; j <= 7; j++){
                    printHighlightBoard(board, currentPosition, validMoves, out, i, j);
                }
                if(i>0 && i<9) {
                    out.print(boardEdgeColor);
                    out.print(SET_TEXT_COLOR_BLACK);
                    printLetterWithSpaces(out, String.valueOf(i));
                    out.print(RESET_BG_COLOR);
                    System.out.println();
                }
            }
        }
        printHeaders(out, teamColor);
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);

    }

    private void printHighlightBoard(ChessBoard board, ChessPosition currentPosition, Collection<ChessMove> validMoves, PrintStream out, int i, int j) {
        if (i > 0 && i < 9) {
            ChessPiece currPiece=board.getPiece(new ChessPosition(i, j + 1));
            if ((i + j) % 2 == 0) {
                out.print(boardWhiteColor);
            } else {
                out.print(boardBlackColor);
            }
            if (validMoves.contains(new ChessMove(currentPosition, new ChessPosition(i, j + 1), null))) {
                if ((i + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_GREEN);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }
            }
            if (!(currPiece == null)) {
                if (new ChessPosition(i, j + 1).equals(currentPosition)) {
                    out.print(SET_BG_COLOR_YELLOW);
                }
                printPiece(out, currPiece.getPieceType(), currPiece.getTeamColor());
                out.print(RESET_BG_COLOR);
            } else {
                out.print(EMPTY);
            }
        }
    }
}