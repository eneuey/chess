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

    private Collection<ChessMove> makePawnMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            if(row == 8) {
                return List.of();
            }
            else {
                ChessPosition nextSquare = new ChessPosition(row + 1, col);
                if(board.getPiece(nextSquare) == null) {
                    moves.add(new ChessMove(myPosition, nextSquare, null));
                }
                ChessPosition leftAttackingSquare = new ChessPosition(row + 1, col - 1);
                if(col > 1 && board.getPiece(leftAttackingSquare) != null && board.getPiece(leftAttackingSquare).pieceColor != pieceColor) {
                    moves.add(new ChessMove(myPosition, leftAttackingSquare, null));
                }
                ChessPosition rightAttackingSquare = new ChessPosition(row + 1,col + 1);
                if(col < 8 && board.getPiece(rightAttackingSquare) != null && board.getPiece(rightAttackingSquare).pieceColor != pieceColor) {
                    moves.add(new ChessMove(myPosition, rightAttackingSquare, null));
                }
                ChessPosition startingExtraSquare = new ChessPosition(row + 2,col );
                if(row == 2 && board.getPiece(startingExtraSquare) == null) {
                    moves.add(new ChessMove(myPosition, startingExtraSquare, null));
                }
            }
        }
        else {
            if(row == 1) {
                return List.of();
            }
            else {
                ChessPosition nextSquare = new ChessPosition(row - 1, col);
                if(board.getPiece(nextSquare) == null) {
                    moves.add(new ChessMove(myPosition, nextSquare, null));
                }
                ChessPosition leftAttackingSquare = new ChessPosition(row - 1, col - 1);
                if(col > 1 && board.getPiece(leftAttackingSquare) != null && board.getPiece(leftAttackingSquare).pieceColor != pieceColor) {
                    moves.add(new ChessMove(myPosition, leftAttackingSquare, null));
                }
                ChessPosition rightAttackingSquare = new ChessPosition(row - 1,col + 1);
                if(col < 8 && board.getPiece(rightAttackingSquare) != null && board.getPiece(rightAttackingSquare).pieceColor != pieceColor) {
                    moves.add(new ChessMove(myPosition, rightAttackingSquare, null));
                }
                ChessPosition startingExtraSquare = new ChessPosition(row - 2,col );
                if(row == 7 && board.getPiece(startingExtraSquare) == null) {
                    moves.add(new ChessMove(myPosition, startingExtraSquare, null));
                }
            }
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
