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

    private Collection<ChessMove> makeChessMove(ChessBoard board, ChessPosition myPosition, int[][] moveList, boolean allowRange) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        for(int[] moveDirection : moveList) {
            int row = myPosition.getRow() + moveDirection[0];
            int col = myPosition.getColumn() + moveDirection[1];
            boolean validSquare = true;
            while (col >= 1 && col <= 8 && row >= 1 && row <= 8 && validSquare) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                if (board.getPiece(currentPosition) == null) {
                    moves.add(new ChessMove(myPosition, currentPosition, null));
                } else if (board.getPiece(currentPosition).pieceColor != this.pieceColor) {
                    moves.add(new ChessMove(myPosition, currentPosition, null));
                    validSquare = false;
                } else {
                    validSquare = false;
                }
                if(!allowRange) {
                    validSquare = false;
                }
                row += moveDirection[0];
                col += moveDirection[1];
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnPromotionCheck(ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        if(endPosition.getRow() == 8 || endPosition.getRow() == 1) {
            moves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
            moves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
            moves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
            moves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
        }
        else {
            moves.add(new ChessMove(startPosition, endPosition, null));
        }
        return moves;
    }

    private Collection<ChessMove> makePawnMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int direction = 0;
        if(pieceColor == ChessGame.TeamColor.WHITE) direction++; else direction--;
        //Check if an extra starting move is available
        if((row == 2 || row == 7) && row + (2 * direction) < 8 && row + (2 * direction) > 0) {
            ChessPosition startingExtraSquare = new ChessPosition(row + (2 * direction),col );
            if(board.getPiece(startingExtraSquare) == null && board.getPiece(new ChessPosition(row + direction, col)) == null) {
                moves.add(new ChessMove(myPosition, startingExtraSquare, null));
            }
        }
        //Check if one square forward move is available and if results in promotion
        ChessPosition nextSquare = new ChessPosition(row + direction, col);
        if(board.getPiece(nextSquare) == null) {
            moves.addAll(pawnPromotionCheck(myPosition, nextSquare));
        }
        //checking if pawn can attack to the left and if it results in promotion
        ChessPosition leftAttackingSquare = new ChessPosition(row + direction, col - 1);
        if(col > 1 && board.getPiece(leftAttackingSquare) != null && board.getPiece(leftAttackingSquare).pieceColor != pieceColor) {
            moves.addAll(pawnPromotionCheck(myPosition, leftAttackingSquare));
        }
        //checking if pawn can attack to the right and if it results in promotion
        ChessPosition rightAttackingSquare = new ChessPosition(row + direction,col + 1);
        if(col < 8 && board.getPiece(rightAttackingSquare) != null && board.getPiece(rightAttackingSquare).pieceColor != pieceColor) {
            moves.addAll(pawnPromotionCheck(myPosition, rightAttackingSquare));

        }
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
        if(type == PieceType.BISHOP) {
            int[][] bishopMoves = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
            return makeChessMove(board, myPosition, bishopMoves, true);
        }
        if(type == PieceType.ROOK) {
            int[][] rookMoves = {{1,0}, {0,1}, {-1,0}, {0,-1}};
            return makeChessMove(board, myPosition, rookMoves, true);
        }
        if(type == PieceType.QUEEN) {
            int[][] queenMoves = {{1,0}, {0,1}, {-1,0}, {0,-1},{1,1}, {1,-1}, {-1,1}, {-1,-1}};
            return makeChessMove(board, myPosition, queenMoves, true);
        }
        if(type == PieceType.KING) {
            int[][] kingMoves = {{1,0}, {0,1}, {-1,0}, {0,-1},{1,1}, {1,-1}, {-1,1}, {-1,-1}};
            return makeChessMove(board, myPosition, kingMoves, false);
        }
        if(type == PieceType.KNIGHT) {
            int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1},{1,2}, {1,-2}, {-1,2}, {-1,-2}};
            return makeChessMove(board, myPosition, knightMoves, false);
        }
        if(type == PieceType.PAWN) {
            return makePawnMove(board, myPosition);
        }
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
