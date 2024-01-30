package ui;

import chess.*;

import java.util.Map;

public class ChessBoardDrawer {

    private static final int BOARD_SIZE = 8;
    private static final String WHITE_SQUARE = EscapeSequences.SET_BG_COLOR_WHITE;
    private static final String BLACK_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK;
    private static final String BORDER_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;

    private ChessGame game = new Game();

    public void drawBoard(Integer gameID, String playerColor, ChessGame game) {
        if (gameID == null) {
            return;
        }
        this.game = game;
        System.out.print(EscapeSequences.ERASE_SCREEN);

        if ("WHITE".equals(playerColor)) {
            whiteBoard(gameID);
        } else if ("BLACK".equals(playerColor)) {
            blackBoard(gameID);
        } else {
            whiteBoard(gameID);
        }
    }

    public void whiteBoard(Integer gameID) {

        if (gameID != null) {
            ChessBoard board = game.getBoard();
            System.out.print(EscapeSequences.ERASE_SCREEN);
            displayFlippedColumnLetter();

            for (int row = BOARD_SIZE; row >= 1; row--) {
                displayFlippedRowNumber(row);

                for (int col = 1; col <= BOARD_SIZE; col++) {
                    chessSquare(row, col, board);
                    System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
                }
                displayFlippedRowNumber(row);
                System.out.println();
            }
            displayFlippedColumnLetter();
        }
    }

    private void chessSquare(int row, int col, ChessBoard board) {
        String square = ((row + col) % 2 == 0) ? WHITE_SQUARE : BLACK_SQUARE;
        System.out.print(square);

        ChessPosition position = new Position(row, col);
        ChessPiece piece = board.getPiece(position);

        if (piece != null) {
            String pieceSymbol = getPieceSymbols(piece);
            String pieceColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.SET_TEXT_COLOR_BLUE
                    : EscapeSequences.SET_TEXT_COLOR_RED;
            System.out.print(pieceColor + pieceSymbol + EscapeSequences.SET_TEXT_BOLD);
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
        System.out.print(EscapeSequences.EMPTY);
    }

    private void extraSquare(String color) {
        System.out.print(color);
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }

    private void displayRowNumber(int row) {
        System.out.print(BORDER_COLOR);
        System.out.print(" " + (BOARD_SIZE - row + 1) + " ");
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }

    private void displayColumnLetter() {
        System.out.print(BORDER_COLOR);
        extraSquare(BORDER_COLOR);

        for (int col = 1; col <= BOARD_SIZE; col++) {
            System.out.print(BORDER_COLOR);
            System.out.print(" " + " " + " " + (char) ('h' - col + 1) + " " + " ");
            System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
        }
        extraSquare(BORDER_COLOR);
        System.out.println();
    }

    // ------------------------- FLIPPED BOARD ------------------------- //

    public void blackBoard(Integer gameID) {
        if (gameID != null) {
            ChessBoard board = game.getBoard();
            System.out.print(EscapeSequences.ERASE_SCREEN);
            displayColumnLetter();

            for (int row = 1; row <= BOARD_SIZE; row++) {
                displayFlippedRowNumber(row);

                for (int col = BOARD_SIZE; col >= 1; col--) {
                    chessSquare(row, col, board);
                    System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
                }
                displayFlippedRowNumber(row);
                System.out.println();
            }
            displayColumnLetter();
        }
    }

    public void displayFlippedRowNumber(int row) {
        System.out.print(BORDER_COLOR);
        System.out.print(" " + (row) + " ");
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }

    public void displayFlippedColumnLetter() {
        System.out.print(BORDER_COLOR);
        extraSquare(BORDER_COLOR);
        for (int col = BOARD_SIZE; col >= 1; col--) {
            System.out.print(BORDER_COLOR);
            System.out.print(" " + " " + " " + (char) ('h' - col + 1) + " " + " ");
            System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
        }
        extraSquare(BORDER_COLOR);
        System.out.println();
    }

    private String getPieceSymbols(ChessPiece piece) {
        Map<ChessPiece.PieceType, String> symbolMap = Map.of(
                ChessPiece.PieceType.KING, EscapeSequences.WHITE_KING,
                ChessPiece.PieceType.QUEEN, EscapeSequences.WHITE_QUEEN,
                ChessPiece.PieceType.BISHOP, EscapeSequences.WHITE_BISHOP,
                ChessPiece.PieceType.KNIGHT, EscapeSequences.WHITE_KNIGHT,
                ChessPiece.PieceType.ROOK, EscapeSequences.WHITE_ROOK,
                ChessPiece.PieceType.PAWN, EscapeSequences.WHITE_PAWN
        );

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            symbolMap = Map.of(
                    ChessPiece.PieceType.KING, EscapeSequences.BLACK_KING,
                    ChessPiece.PieceType.QUEEN, EscapeSequences.BLACK_QUEEN,
                    ChessPiece.PieceType.BISHOP, EscapeSequences.BLACK_BISHOP,
                    ChessPiece.PieceType.KNIGHT, EscapeSequences.BLACK_KNIGHT,
                    ChessPiece.PieceType.ROOK, EscapeSequences.BLACK_ROOK,
                    ChessPiece.PieceType.PAWN, EscapeSequences.BLACK_PAWN
            );
        }

        return symbolMap.get(piece.getPieceType());
    }
}