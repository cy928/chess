package moves;
import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends ChessPiece {
  public Queen(ChessGame.TeamColor pieceColor) {
    super(pieceColor, PieceType.QUEEN);
  }

  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    List<ChessMove> validMoves = new ArrayList<>();

    int[][] directions={{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    validMoves = helperForQueenAndRookAndBishop(board, validMoves, directions, myPosition);
    return validMoves;
  }
}

