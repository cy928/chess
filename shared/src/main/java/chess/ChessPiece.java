package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import moves.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType pieceType;
    private final ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (this.getPieceType() == PieceType.BISHOP) {
            Bishop move = new Bishop(this.getTeamColor());
            moves.addAll(move.pieceMoves(board, myPosition));
        } else if (this.getPieceType() == PieceType.KING) {
            King move = new King(this.getTeamColor());
            moves.addAll(move.pieceMoves(board, myPosition));
        } else if (this.getPieceType() == PieceType.KNIGHT) {
            Knight move = new Knight(this.getTeamColor());
            moves.addAll(move.pieceMoves(board, myPosition));
        } else if (this.getPieceType() == PieceType.ROOK) {
            Rook move = new Rook(this.getTeamColor());
            moves.addAll(move.pieceMoves(board, myPosition));
        } else if (this.getPieceType() == PieceType.QUEEN) {
            Queen move = new Queen(this.getTeamColor());
            moves.addAll(move.pieceMoves(board, myPosition));
        } else if (this.getPieceType() == PieceType.PAWN) {
            Pawn move = new Pawn(this.getTeamColor());
            moves.addAll(move.pieceMoves(board, myPosition));
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }

    public List<ChessMove> helperForQueenAndRookAndBishop(ChessBoard board, List<ChessMove> validMoves, int[][] directions, ChessPosition myPosition) {
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

    public List<ChessMove> helperForKingAndKnight(ChessBoard board, List<ChessMove> validMoves, int[][] directions, ChessPosition myPosition) {
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
