package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    private Collection<ChessMove> moveChecker(ChessBoard board, ChessPosition myPosition, int rowIterator, int colIterator) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow() + rowIterator;
        int col = myPosition.getColumn() + colIterator;
        boolean validSquare = true;
        while( col >= 1 && col  <= 8 && row >= 1 && row <= 8 && validSquare) {
            ChessPosition currentPosition = new ChessPosition(row, col);
            if(board.getPiece(currentPosition) == null) {
                moves.add(new ChessMove(myPosition, currentPosition, null));
            }
            else if(board.getPiece(currentPosition).pieceColor != this.pieceColor) {
                moves.add(new ChessMove(myPosition, currentPosition, null));
                validSquare = false;
            }
            else {
                validSquare = false;
            }
            row += rowIterator;
            col += colIterator;
        }
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(moveChecker(board, myPosition, 1, 1));
        moves.addAll(moveChecker(board, myPosition, 1, -1));
        moves.addAll(moveChecker(board, myPosition, -1, 1));
        moves.addAll(moveChecker(board, myPosition, -1, -1));
        return moves;
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(moveChecker(board, myPosition, 1, 0));
        moves.addAll(moveChecker(board, myPosition, -1, 0));
        moves.addAll(moveChecker(board, myPosition, 0, 1));
        moves.addAll(moveChecker(board, myPosition, 0, -1));
        return moves;
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(bishopMoves(board, myPosition));
        moves.addAll(rookMoves(board, myPosition));
        return moves;
    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(type == PieceType.BISHOP) {return bishopMoves(board, myPosition);}
        if(type == PieceType.ROOK) {return rookMoves(board, myPosition);}
        if(type == PieceType.QUEEN) {return queenMoves(board, myPosition);}
        if(type == PieceType.KING) {return kingMoves(board, myPosition);}

        return List.of();
    }

    @Override
    public String toString() {
        String piece = "";
        if(type == PieceType.BISHOP) {piece = "b";}
        if(type == PieceType.KNIGHT) {piece = "n";}
        if(type == PieceType.ROOK) {piece = "r";}
        if(type == PieceType.QUEEN) {piece = "q";}
        if(type == PieceType.KING) {piece = "k";}
        if(type == PieceType.PAWN) {piece = "p";}
        if(pieceColor == ChessGame.TeamColor.WHITE) {piece = piece.toUpperCase();}
        return piece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
