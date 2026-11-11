package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.DISPLAY_BLACK_PAWN;
import static ui.EscapeSequences.DISPLAY_BLACK_ROOK;

public class BoardRenderer {

    public void render(ChessBoard board, ChessGame.TeamColor color) {
        String[][] generatedBoard = generateBoard(board);
        printColumnLabel(color);
        if(color == ChessGame.TeamColor.WHITE) {
            for (int i = generatedBoard.length - 1; i >= 0; i--) {
                System.out.print(SET_BG_COLOR_DARK_GREY + EM_SPACE + (i + 1) + " " + RESET_BG_COLOR);
                for (int j = 0; j < generatedBoard[i].length; j++) {
                    System.out.print(generatedBoard[i][j]);
                }
                System.out.println(SET_BG_COLOR_DARK_GREY + " " + (i + 1) + EM_SPACE + RESET_BG_COLOR);
            }
        }
        else {
            for (int i = 0; i < generatedBoard.length; i++) {
                System.out.print(SET_BG_COLOR_DARK_GREY + EM_SPACE + (i + 1) + " " + RESET_BG_COLOR);
                for (int j = generatedBoard[i].length - 1; j >= 0; j--) {
                    System.out.print(generatedBoard[i][j]);
                }
                System.out.println(SET_BG_COLOR_DARK_GREY + " " + (i + 1) + EM_SPACE + RESET_BG_COLOR);
            }
        }
        printColumnLabel(color);
    }
    private String[][] generateBoard(ChessBoard board) {
        String[][] generatedBoard = new String[8][8];
        boolean isWhiteSquare = true;
        for(int i = 0; i < generatedBoard.length; i++) {
            isWhiteSquare = !isWhiteSquare;
            for(int j = 0; j < generatedBoard[i].length; j++) {
                ChessPosition currentPos = new ChessPosition(i + 1, j + 1);
                generatedBoard[i][j] = getBackgroud(isWhiteSquare) + getPiece(board.getPiece(currentPos)) + RESET_BG_COLOR;
                isWhiteSquare = !isWhiteSquare;
            }
        }
        return generatedBoard;
    }
    private String getBackgroud(boolean isWhiteSquare) {
        if(isWhiteSquare) {
            return SET_BG_COLOR_TAN;
        }
        else {
            return SET_BG_COLOR_DARK_GREEN;
        }
    }
    private String getPiece(ChessPiece piece) {
        String square;
        if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPiece.PieceType pieceType = piece.getPieceType();
            square = switch (pieceType) {
                case KING -> DISPLAY_WHITE_KING;
                case QUEEN -> DISPLAY_WHITE_QUEEN;
                case BISHOP -> DISPLAY_WHITE_BISHOP;
                case KNIGHT -> DISPLAY_WHITE_KNIGHT;
                case ROOK -> DISPLAY_WHITE_ROOK;
                case PAWN -> DISPLAY_WHITE_PAWN;
            };
        }
        else if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPiece.PieceType pieceType = piece.getPieceType();
            square = switch (pieceType) {
                case KING -> DISPLAY_BLACK_KING;
                case QUEEN -> DISPLAY_BLACK_QUEEN;
                case BISHOP -> DISPLAY_BLACK_BISHOP;
                case KNIGHT -> DISPLAY_BLACK_KNIGHT;
                case ROOK -> DISPLAY_BLACK_ROOK;
                case PAWN -> DISPLAY_BLACK_PAWN;
            };
        }
        else {
            square = EMPTY;
        }
        return square;
    }
    private void printColumnLabel(ChessGame.TeamColor color) {
        System.out.print(SET_BG_COLOR_DARK_GREY + EMPTY);
        if(color == ChessGame.TeamColor.WHITE) {
            for(int i = 0; i < 8; i++) {
                System.out.print(" " + (char)(97 + i) + EM_SPACE);
            }
        }
        else {
            for(int i = 7; i >= 0; i--) {
                System.out.print(" " + (char)(97 + i) + EM_SPACE);
            }
        }
        System.out.println(EMPTY + RESET_BG_COLOR);
    }
}
