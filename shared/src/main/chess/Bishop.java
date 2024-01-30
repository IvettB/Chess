package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(ChessGame.TeamColor color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        //Define the possible diagonal moves
        int[] rowDirection = {-1, -1, 1, 1};
        int[] colDirection = {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++) {
            //Position myPosition = (Position) myPosition;
            int newRow = myPosition.row() + rowDirection[i];
            int newCol = myPosition.column() + colDirection[i];

            while (isValidPosition(newRow, newCol)) {
                Position newPosition = new Position(newRow, newCol);
                Piece targetPiece = (Piece) board.getPiece(newPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new Move(myPosition, newPosition, null);
                    validMoves.add(move);
                }

                if (targetPiece != null) {
                    break;
                }

                newRow += rowDirection[i];
                newCol += colDirection[i];
            }
        }
        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
