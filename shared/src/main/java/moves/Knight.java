package moves;
import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends ChessPiece {
  public Knight(ChessGame.TeamColor pieceColor) {
    super(pieceColor, PieceType.KNIGHT);
  }

  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    List<ChessMove> validMoves = new ArrayList<>();

    int[][] directions={{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

    validMoves = helperForKingAndKnight(board, validMoves, directions, myPosition);
    return validMoves;
  }

  private boolean isValidMove(int row, int col) {
    return row >= 1 && row <= 8 & col >= 1 && col <= 8;
  }
}

