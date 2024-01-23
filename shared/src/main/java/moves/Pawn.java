package moves;
import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class Pawn extends ChessPiece {
  private boolean firstMove = true;
  public Pawn(ChessGame.TeamColor pieceColor) {
    super(pieceColor, PieceType.PAWN);
  }
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    List<ChessMove> validMoves = new ArrayList<>();

    int direction = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;

    // Move forward one square
    addPawnMove(board, validMoves, myPosition, direction, 1);

    // Capture diagonally
    addPawnCapture(board, validMoves, myPosition, direction, -1);
    addPawnCapture(board, validMoves, myPosition, direction, 1);

    // Pawn promotion
    if (myPosition.getRow() + direction == 8 || myPosition.getRow() + direction == 1) {
      promotePawn(validMoves);
    }


    return validMoves;
  }

  private void addPawnMove(ChessBoard board, List<ChessMove> moves, ChessPosition position, int direction, int steps) {
    int newRow = position.getRow() + direction;
    int newCol = position.getColumn();

    if (isValidMove(newRow, newCol) && board.getPiece(new ChessPosition(newRow, newCol)) == null) {
      moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));

      // Check for the special case to move forward two squares
      if ((getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) ||
              (getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7)) {
        int newRowTwoSteps = newRow + direction;
        int newColTwoSteps = position.getColumn();

        if (isValidMove(newRowTwoSteps, newColTwoSteps) && board.getPiece(new ChessPosition(newRowTwoSteps, newColTwoSteps)) == null) {
          moves.add(new ChessMove(position, new ChessPosition(newRowTwoSteps, newColTwoSteps), null));
        }
      }
    }
  }

  private void addPawnCapture(ChessBoard board, List<ChessMove> moves, ChessPosition position, int direction, int captureCol) {
    int newRow = position.getRow() + direction;
    int newCol = position.getColumn() + captureCol;

    if (isValidMove(newRow, newCol)) {
      ChessPiece targetPiece = board.getPiece(new ChessPosition(newRow, newCol));
      if (targetPiece != null && targetPiece.getTeamColor() != getTeamColor()) {
        moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
      }
    }
  }

  private void promotePawn(List<ChessMove> moves) {
    // Add promotion moves for Rook, Knight, Bishop, and Queen
    for (ChessMove move : new ArrayList<>(moves)) {
      moves.remove(move);
      moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.ROOK));
      moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.KNIGHT));
      moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.BISHOP));
      moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.QUEEN));
    }
  }

  private boolean isValidMove(int row, int col) {
    return row >= 1 && row <= 8 && col >= 1 && col <= 8;
  }
}