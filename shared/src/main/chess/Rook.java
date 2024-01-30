package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece {
    public Rook(ChessGame.TeamColor color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        int[] rowDirection = {-1, 1, 0, 0};
        int[] colDirection = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            //Position pos = (Position) myPosition;
            int newRow = myPosition.row() + rowDirection[i];
            int newCol = myPosition.column() + colDirection[i];

            while (isValidPosition(newRow, newCol)) {
                Position newPosition = new Position(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

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

    public boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
