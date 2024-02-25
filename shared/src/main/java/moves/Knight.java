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

    for (int[] direction : directions) {
      int newRow = myPosition.getRow() + direction[0];
      int newCol = myPosition.getColumn() + direction[1];

      if (isValidMove(newRow, newCol)) {
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessPiece targetPiece = board.getPiece(newPosition);

        if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
          validMoves.add(new ChessMove(myPosition, newPosition, null));
        }
      }
    }
    return validMoves;
  }

  private boolean isValidMove(int row, int col) {
    return row >= 1 && row <= 8 & col >= 1 && col <= 8;
  }
}

