package moves;
import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends ChessPiece {
  public Rook(ChessGame.TeamColor pieceColor) {
    super(pieceColor, PieceType.ROOK);
  }

  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    List<ChessMove> validMoves = new ArrayList<>();

    int[][] directions={{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    for (int[] direction : directions) {
      int newRow = myPosition.getRow();
      int newCol = myPosition.getColumn();
      while (true) {
        newRow += direction[0];
        newCol += direction[1];
        if (!isValidMove(newRow, newCol)) {
          break;
        }
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessPiece targetPiece = board.getPiece(newPosition);
        if (targetPiece == null) {
          validMoves.add(new ChessMove(myPosition, newPosition, null));
        } else {
          if (targetPiece.getTeamColor() != getTeamColor()) {
            validMoves.add(new ChessMove(myPosition, newPosition, null));
          }
          break;
        }
      }
    }
    return validMoves;
  }

  private boolean isValidMove(int row, int col) {
    return row >= 1 && row <= 8 & col >= 1 && col <= 8;
  }
}

