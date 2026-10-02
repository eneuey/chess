package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard other) {
        board = new ChessPiece[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(other.board[i][j] != null) {
                    ChessGame.TeamColor color = other.board[i][j].getTeamColor();
                    ChessPiece.PieceType type = other.board[i][j].getPieceType();
                    this.board[i][j] = new ChessPiece(color, type);
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[][] startingSet = new ChessPiece.PieceType[][]{{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                                                                    ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                                                                    ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                                                                    ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK},
                                                                    {ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN,
                                                                    ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN,
                                                                    ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN,
                                                                    ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN}};
        ChessPiece[][] newBoard = new ChessPiece[8][8];
        for(int i = 0; i < startingSet.length; i++) {
            for(int j = 0; j < 8; j++) {
                newBoard[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, startingSet[i][j]);
            }
        }
        int rowNum = 7;
        for (ChessPiece.PieceType[] pieceTypes : startingSet) {
            for (int j = 0; j < 8; j++) {
                newBoard[rowNum][j] = new ChessPiece(ChessGame.TeamColor.BLACK, pieceTypes[j]);
            }
            rowNum--;
        }
        board = newBoard;
    }

    @Override
    public String toString() {
        StringBuilder boardDisplay = new StringBuilder();
        for(int i = board.length - 1; i >= 0; i--) {
            for(int j = 0; j < board[i].length; j++) {
                boardDisplay.append("[");
                if(board[i][j] == null) {
                    boardDisplay.append(" ");
                }
                else {
                    boardDisplay.append(board[i][j].toString());
                }
                boardDisplay.append("]");
            }
            boardDisplay.append("\n");
        }
        return boardDisplay.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
