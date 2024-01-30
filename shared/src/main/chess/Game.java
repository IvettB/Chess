package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Game implements ChessGame {

    private ChessBoard chessBoard = new Board();
    private TeamColor teamTurn;
    private boolean markEndOfGame = false;

    public Game() {
        this.teamTurn = TeamColor.WHITE;
        chessBoard.resetBoard();
    }

    public boolean isMarkEndOfGame() {
        return markEndOfGame;
    }

    public void setMarkEndOfGame(boolean markEndOfGame) {
        this.markEndOfGame = markEndOfGame;
    }

    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        if (piece == null) {
            return Collections.emptyList();
        }
        Collection<ChessMove> moves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> finalMoves = new ArrayList<>();
        for (ChessMove move : moves) {
            chessBoard.addPiece(startPosition, null);
            ChessPiece position = chessBoard.getPiece(move.getEndPosition());
            chessBoard.addPiece(move.getEndPosition(), piece);
            if (!isInCheck(piece.getTeamColor())) {
                finalMoves.add(move);
            }
            chessBoard.addPiece(startPosition, piece);
            chessBoard.addPiece(move.getEndPosition(), position);
        }
        return finalMoves;
    }


    @Override
    public boolean isValidMove(Move moves) {
        Collection<ChessMove> finalMoves = validMoves(moves.getStartPosition());
        if (!finalMoves.isEmpty() && finalMoves.contains(moves)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());

        if (validMoves.contains(move) && piece != null && piece.getTeamColor() == teamTurn) {
            chessBoard.addPiece(move.getEndPosition(), piece);
            chessBoard.removePiece(move.getStartPosition());

            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                int promotionRank = (piece.getTeamColor() == TeamColor.WHITE) ? 8 : 1;
                if (move.getEndPosition().row() == promotionRank) {
                    ChessPiece promotedPiece = createPromotedPiece(move.getPromotionPiece(), piece.getTeamColor());
                    chessBoard.addPiece(move.getEndPosition(), promotedPiece);
                }
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    private ChessPiece createPromotedPiece(ChessPiece.PieceType type, TeamColor teamColor) {
        return switch (type) {
            case ROOK -> new Rook(teamColor);
            case KNIGHT -> new Knight(teamColor);
            case BISHOP -> new Bishop(teamColor);
            default -> new Queen(teamColor);
        };
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = chessBoard.getPiece(new Position(i, j));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (piece instanceof King) {
                        kingPosition = new Position(i, j);
                        break;
                    }
                }
            }
            if (kingPosition != null) {
                break;
            }
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = chessBoard.getPiece(new Position(i, j));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(chessBoard, new Position(i, j));
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPiece piece = chessBoard.getPiece(new Position(i, j));
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> moves = validMoves(new Position(i, j));
                        if (!moves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = chessBoard.getPiece(new Position(i, j));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new Position(i, j));
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        chessBoard = (Board) board;
    }

    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
