package moves;
import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends ChessPiece {
  public King(ChessGame.TeamColor pieceColor) {
    super(pieceColor, PieceType.KING);
  }

  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    List<ChessMove> validMoves = new ArrayList<>();

    int[][] directions={{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    validMoves = helperForKingAndKnight(board, validMoves, directions, myPosition);
    return validMoves;
  }

  private boolean isValidMove(int row, int col) {
    return row >= 1 && row <= 8 & col >= 1 && col <= 8;
  }
}

