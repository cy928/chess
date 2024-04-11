package moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends ChessPiece {
  public Bishop(ChessGame.TeamColor pieceColor) {
    super(pieceColor, PieceType.BISHOP);
  }

  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    List<ChessMove> validMoves=new ArrayList<>();

    int[][] directions={{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    validMoves = helperForQueenAndRookAndBishop(board, validMoves, directions, myPosition);
    return validMoves;
  }

  private boolean isValidMove(int row, int col) {
    return row >= 1 && row <= 8 & col >= 1 && col <= 8;
  }
}

