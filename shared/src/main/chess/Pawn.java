package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(ChessGame.TeamColor color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        int direction = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        //Position pos = (Position) myPosition;
        int newRow = myPosition.row() + direction;
        int newCol = myPosition.column();

        if (isValidPosition(newRow, newCol) && board.getPiece(new Position(newRow, newCol)) == null) {
            ChessPosition newPosition = new Position(newRow, newCol);
            ChessMove move = new Move(myPosition, newPosition, null);
            validMoves.add(move);

            if ((getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.row() == 2) ||
                    (getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.row() == 7)) {
                int doubleMoveRow = myPosition.row() + (2 * direction);
                int doubleMoveCol = myPosition.column();

                if (isValidPosition(doubleMoveRow, doubleMoveCol) && board.getPiece(new Position(doubleMoveRow, doubleMoveCol)) == null) {
                    ChessPosition doubleMovePosition = new Position(doubleMoveRow, doubleMoveCol);
                    ChessMove doubleMove = new Move(myPosition, doubleMovePosition, null);
                    validMoves.add(doubleMove);
                }
            }
        }

        int[] colOffsets = {-1, 1};
        for (int colOffset : colOffsets) {
            newRow = myPosition.row() + direction;
            newCol = myPosition.column() + colOffset;

            if (isValidPosition(newRow, newCol)) {
                ChessPosition newPosition = new Position(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece != null && targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove captureMove = new Move(myPosition, newPosition, null);
                    validMoves.add(captureMove);
                }
            }
        }

        List<ChessMove> promotions = new ArrayList<>();
        if (ReachesPromotionRow(myPosition)) {
            for (ChessMove move : validMoves) {
                getPromotionPieces(move, promotions);
            }
            return promotions;
        }
        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private boolean ReachesPromotionRow(ChessPosition myPosition) {
        Position pos = (Position) myPosition;
        return (getTeamColor() == ChessGame.TeamColor.WHITE && pos.row() == 7) ||
                (getTeamColor() == ChessGame.TeamColor.BLACK && pos.row() == 2);
    }

    private void getPromotionPieces(ChessMove move, List<ChessMove> promotions) {
        promotions.add(new Move(move.getStartPosition(), move.getEndPosition(), PieceType.QUEEN));
        promotions.add(new Move(move.getStartPosition(), move.getEndPosition(), PieceType.ROOK));
        promotions.add(new Move(move.getStartPosition(), move.getEndPosition(), PieceType.KNIGHT));
        promotions.add(new Move(move.getStartPosition(), move.getEndPosition(), PieceType.BISHOP));
    }
}
