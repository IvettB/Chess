package chess;

import java.util.Objects;

public class Move implements ChessMove {

    private final Position startPosition;
    private final Position endPosition;
    private final ChessPiece.PieceType getPiece;

    public Move(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType getPiece) {
        this.startPosition = (Position) startPosition;
        this.endPosition = (Position) endPosition;
        this.getPiece = getPiece;
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return getPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(startPosition, move.startPosition) && Objects.equals(endPosition, move.endPosition) && Objects.equals(getPiece, move.getPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, getPiece);
    }
}
