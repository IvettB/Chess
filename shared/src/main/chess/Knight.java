package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {
    public Knight(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        int[] rowOffsets = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] colOffsets = {-1, 1, -2, 2, -2, 2, 1, -1};

        for (int i = 0; i < 8; i++) {
            //Position pos = (Position) myPosition;
            int newRow = myPosition.row() + rowOffsets[i];
            int newCol = myPosition.column() + colOffsets[i];

            if (isValidPosition(newRow, newCol)) {
                Position newPosition = new Position(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new Move(myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
