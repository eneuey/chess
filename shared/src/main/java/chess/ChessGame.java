package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    private boolean noTeamValidMoves(TeamColor teamColor) {
        boolean noValidTeamMoves = true;
        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition currentPos = new ChessPosition(i,j);
                if(board.getPiece(currentPos) != null && board.getPiece(currentPos).getTeamColor() == teamColor) {
                    ChessPiece piece = board.getPiece(currentPos);
                    Collection<ChessMove> possibleMoves = validMoves(currentPos);
                    if(!possibleMoves.isEmpty()) {
                        noValidTeamMoves = false;
                    }
                }
            }
        }
        return noValidTeamMoves;
    }

    private void movePiece(ChessMove move) {
        ChessPosition startingPos = move.getStartPosition();
        ChessPosition endingPos = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(startingPos);
        TeamColor color = piece.getTeamColor();
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN && (endingPos.getRow() == 1 || endingPos.getRow() == 8)) {
            piece = new ChessPiece(color, promotionPiece);
        }
        board.addPiece(startingPos, null);
        board.addPiece(endingPos, piece);
    }

    private Collection<ChessPosition> getEndPositions(ChessPosition position) {
        Collection<ChessMove> possibleMoves = board.getPiece(position).pieceMoves(board, position);
        Collection<ChessPosition> endPositions = new ArrayList<>();
        for(ChessMove move : possibleMoves) {
            endPositions.add(move.getEndPosition());
        }
        return endPositions;
    }

    private Collection<ChessPosition> getEnemyAttackingSquares(TeamColor teamColor) {
        Collection<ChessPosition> enemyAttackingSquares = new ArrayList<>();
        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != teamColor) {
                    enemyAttackingSquares.addAll(getEndPositions(currentPosition));
                }
            }
        }
        return enemyAttackingSquares;
    }
    private ChessPosition getKingPosition(TeamColor teamColor) {
        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currentPosition).getTeamColor() == teamColor) {
                    return currentPosition;
                }
            }
        }
        return null;
    }

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }
    public ChessGame(ChessGame other) {
        this.board = new ChessBoard(other.board);
        this.teamTurn = other.teamTurn;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if(board.getPiece(startPosition) != null) {
            Collection<ChessMove> potentialMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
            for (ChessMove potentialMove : potentialMoves) {
                ChessGame testGame = new ChessGame(this);
                testGame.movePiece(potentialMove);
                if (!testGame.isInCheck(board.getPiece(startPosition).getTeamColor())) {
                    validMoves.add(potentialMove);
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startingPosition = move.getStartPosition();
        Collection<ChessMove> possibleMoves = validMoves(startingPosition);
        if(board.getPiece(startingPosition) != null && board.getPiece(startingPosition).getTeamColor() == teamTurn && possibleMoves.contains(move)) {
            movePiece(move);
            if(teamTurn == TeamColor.WHITE) teamTurn = TeamColor.BLACK; else teamTurn = TeamColor.WHITE;
        }
        else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return getEnemyAttackingSquares(teamColor).contains(getKingPosition(teamColor));
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noTeamValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noTeamValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
