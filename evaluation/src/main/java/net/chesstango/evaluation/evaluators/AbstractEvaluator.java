package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractEvaluator implements GameEvaluator {
    public static int evaluateFinalStatus(final Game game) {
        return switch (game.getStatus()) {
            case MATE ->
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                    Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
            case DRAW -> 0;
            default -> throw new RuntimeException("Game is still in progress");
        };
    }

    protected int evaluateByMaterial(final Game game) {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);

        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_WHITE);

        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_BLACK);

        return evaluation;
    }

    abstract int getPieceValue(Piece piece);
}
