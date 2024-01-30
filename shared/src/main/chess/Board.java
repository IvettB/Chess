package chess;

public class Board implements ChessBoard {
    private final ChessPiece[][] chessBoard;

    public Board() {
        chessBoard = new Piece[8][8];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //Position pos = (Position) position;
        chessBoard[position.row() - 1][position.column() - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        //Position pos = (Position) position;
        int row = position.row() - 1;
        int col = position.column() - 1;
        return chessBoard[row][col];
    }

    public void removePiece(ChessPosition position) {
        //Position pos = (Position) position;
        chessBoard[position.row() - 1][position.column() - 1] = null;
    }

    @Override
    public void resetBoard() {
        // Clear the chess board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard[i][j] = null;
            }
        }

        //place individual pieces on the correct square:
        chessBoard[0][4] = new King(ChessGame.TeamColor.WHITE);
        chessBoard[7][4] = new King(ChessGame.TeamColor.BLACK);

        chessBoard[0][3] = new Queen(ChessGame.TeamColor.WHITE);
        chessBoard[7][3] = new Queen(ChessGame.TeamColor.BLACK);

        chessBoard[0][2] = new Bishop(ChessGame.TeamColor.WHITE);
        chessBoard[7][2] = new Bishop(ChessGame.TeamColor.BLACK);
        chessBoard[0][5] = new Bishop(ChessGame.TeamColor.WHITE);
        chessBoard[7][5] = new Bishop(ChessGame.TeamColor.BLACK);

        chessBoard[0][1] = new Knight(ChessGame.TeamColor.WHITE);
        chessBoard[7][1] = new Knight(ChessGame.TeamColor.BLACK);
        chessBoard[0][6] = new Knight(ChessGame.TeamColor.WHITE);
        chessBoard[7][6] = new Knight(ChessGame.TeamColor.BLACK);

        chessBoard[0][0] = new Rook(ChessGame.TeamColor.WHITE);
        chessBoard[7][0] = new Rook(ChessGame.TeamColor.BLACK);
        chessBoard[0][7] = new Rook(ChessGame.TeamColor.WHITE);
        chessBoard[7][7] = new Rook(ChessGame.TeamColor.BLACK);

        //pawn placement implemented in a for loop:
        for (int i = 0; i < 8; i++) {
            chessBoard[1][i] = new Pawn(ChessGame.TeamColor.WHITE);
            chessBoard[6][i] = new Pawn(ChessGame.TeamColor.BLACK);
        }
    }
}
