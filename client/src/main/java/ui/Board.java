package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class Board {

    public static ChessBoard board = new ChessBoard();
    static
    {
        board.resetBoard();
    }

    private static final String boardBlackColor = SET_BG_COLOR_DARK_GREY;
    private static final String boardWhiteColor = SET_BG_COLOR_WHITE;
    private static final String SeparateColor = RESET_BG_COLOR;
    private static final String boardEdgeColor = SET_BG_COLOR_LIGHT_GREY;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawBoard(board, ChessGame.TeamColor.BLACK);
        printEmptyRows(SeparateColor);
        drawBoard(board, ChessGame.TeamColor.WHITE);
    }


    public static void drawBoard(ChessBoard board, ChessGame.TeamColor teamColor)
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        int [] rowNumber;
        int [] colNumber;
        int detNum;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            detNum = 0;
            rowNumber = new int [] {8,7,6,5,4,3,2,1};
            colNumber = new int [] {1,2,3,4,5,6,7,8};
        } else {
            detNum = 1; // 用来判断row的行数是否是奇数还是偶数来判断body里第一个小空格是白色还是黑色
            rowNumber = new int [] {1,2,3,4,5,6,7,8};
            colNumber = new int [] {8,7,6,5,4,3,2,1};
        }
        printHeaders(out, teamColor);
        for (int i = 0; i <= 9; i++){
            if (i>0 && i<9) {
                out.print(boardEdgeColor);
                out.print(SET_TEXT_COLOR_BLACK);
                printLetterWithSpaces(out, String.valueOf(i));
            }
            for (int j = 0; j <= 7; j++){
                if (i > 0 && i < 9) {
                    ChessPiece currPiece=board.getPiece(new ChessPosition(i - 1, j));
                    if (!(currPiece == null)) {
                        printPiece(out, currPiece.getPieceType(), currPiece.getTeamColor());
                        out.print(RESET_BG_COLOR);
                        System.out.println();
                    } else {
                        if ((i + j) % 2 == 0) {
                            out.print(boardBlackColor);
                            out.print(EMPTY);
                        } else {
                            out.print(boardWhiteColor);
                            out.print(EMPTY);
                        }
                    }
                }

            }
            if(i>0 && i<9) {
                out.print(boardEdgeColor);
                out.print(SET_TEXT_COLOR_BLACK);
                printLetterWithSpaces(out, String.valueOf(i));
                out.print(RESET_BG_COLOR);
                System.out.println();
            }
        }


        // 打印左右两边的rows sequences 和board里的棋子
//        ChessGame.TeamColor currSpaceColor; // 当前每一个小空格里的背景颜色
//        for (int row : rowNumber) // 先打印左边的rows sequence
//        {
//            out.print(boardEdgeColor); // 背景颜色也是灰色  和headers一样
//            out.print(SET_TEXT_COLOR_BLACK); // sequences text颜色是黑色的
//            printLetterWithSpaces(out, String.valueOf(row));    // 将每一个字母都分开打印 将row 数字转换为字符串
//
//            // 接下来开始判断board里开头是黑色还是白色了 开始画棋盘body了
//            if (row % 2 == detNum)  // 不需要知道是0还是1 teamcolor是黑色还是白色. 只要一个不是 那肯定是另一个分支
//            {
//                currSpaceColor = ChessGame.TeamColor.WHITE; // 当前小空格颜色为白色
//            }
//            else
//            {
//                currSpaceColor = ChessGame.TeamColor.BLACK; // 当前小空格颜色为黑色
//            }
//            // 接下来开始搞每一行里的剩余的小格子
//            for (int column: colNumber)
//            {
//                if (currSpaceColor == ChessGame.TeamColor.WHITE) // 如果当前行开头颜色是白色
//                {
//                    out.print(boardBlackColor); // 则下一个棋子颜色是黑色
//                    currSpaceColor = ChessGame.TeamColor.BLACK; // 然后将当前的背景颜色设置为黑色
//                }
//                else
//                {
//                    out.print(boardWhiteColor); // 如果开头棋子是黑色 则下一个棋子是白色
//                    currSpaceColor = ChessGame.TeamColor.WHITE; // 然后将当前背景颜色设置为白色
//                }
//                // 分层好了body里每一个棋子背景颜色以后后开始搞里面的piece call getPiece来得到棋子
//                ChessPiece currPiece = board.getPiece(new ChessPosition(row, column));
//                if (!(currPiece == null)) // 如果确实得到了棋子
//                {
//                    printPiece(out, currPiece.getPieceType(), currPiece.getTeamColor()); // 将当前棋子打印到board里
//                }
//                else
//                {
//                    out.print(EMPTY); // 否则的话则是空的 那么就打印全角空格string 表示空的
//                }
//            }
//
//            // 棋子也都打印好了 接下来就是恢复出厂设置
//            out.print(boardEdgeColor); // 恢复成灰色
//            out.print(EMPTY);
//            out.print(RESET_BG_COLOR);
//            out.println();
//        }
        printHeaders(out, teamColor);
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
        out.print("\u2006");
        out.print("\u2006");
        out.print("\u2006");
        out.print("\u2006");
        out.print("\u2006");
        out.print(letter);
        out.print("\u2006");
        out.print("\u2006");
        out.print("\u2006");
        out.print("\u2006");
        out.print("\u2006");
    }

    /*画每一个piece*/
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
            case KING -> pieceString = BLACK_KING;
            case QUEEN -> pieceString = BLACK_QUEEN;
            case BISHOP -> pieceString = BLACK_BISHOP;
            case ROOK -> pieceString = BLACK_ROOK;
            case KNIGHT -> pieceString = BLACK_KNIGHT;
            case PAWN -> pieceString = BLACK_PAWN;
            default -> pieceString = EMPTY;
        }
        out.print(pieceString);
    }

//    private static void printFooter(PrintStream out) {
//        printEmptyRows(out, boardEdgeColor);
//    }

    private static void printEmptyRows(String color) {
        System.out.print(color);
        for (int i = 0; i <= 9; i++) {
            System.out.print(EMPTY);
        }
        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }
}