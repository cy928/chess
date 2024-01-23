package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;
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
        if (this.getPieceType() == PieceType.KING){
            King move = new King(this.getTeamColor());
            moves.addAll(move.pieceMoves(board,myPosition));
        }
        else if (this.getPieceType()==PieceType.QUEEN){
            Queen move = new Queen(this.getTeamColor());
            moves.addAll(move.pieceMoves(board,myPosition));
        }
        else if (this.getPieceType() == PieceType.BISHOP){
            Bishop move = new Bishop(this.getTeamColor());
            moves.addAll(move.pieceMoves(board,myPosition));
        }
        else if(this.getPieceType() == PieceType.ROOK){
            Rook move = new Rook(this.getTeamColor());
            moves.addAll(move.pieceMoves(board,myPosition));
        }
        else if (this.getPieceType() == PieceType.KNIGHT){
            Knight move = new Knight(this.getTeamColor());
            moves.addAll(move.pieceMoves(board,myPosition));
        }
        else if (this.getPieceType() == PieceType.PAWN){
            Pawn move = new Pawn(this.getTeamColor());
            moves.addAll(move.pieceMoves(board,myPosition));
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
}
