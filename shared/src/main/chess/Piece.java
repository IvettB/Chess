package chess;

import java.util.Collection;

public abstract class Piece implements ChessPiece {

    private final ChessGame.TeamColor color;

    private final PieceType type;

    public Piece(ChessGame.TeamColor color, PieceType piece) {
        this.color = color;
        this.type = piece;
    }


    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
