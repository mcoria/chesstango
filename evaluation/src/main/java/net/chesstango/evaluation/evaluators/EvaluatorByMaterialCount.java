package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByMaterialCount extends AbstractEvaluator {
    @Override
    public int evaluate() {
        if (game.getStatus().isFinalStatus()) {
            return evaluateFinalStatus();
        } else {
                return evaluateByMaterial();
        }
    }

    @Override
    public int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 1;
            case PAWN_BLACK -> -1;
            case KNIGHT_WHITE -> 3;
            case KNIGHT_BLACK -> -3;
            case BISHOP_WHITE -> 3;
            case BISHOP_BLACK -> -3;
            case ROOK_WHITE -> 5;
            case ROOK_BLACK -> -5;
            case QUEEN_WHITE -> 9;
            case QUEEN_BLACK -> -9;
            case KING_WHITE -> 10;
            case KING_BLACK -> -10;
        };
    }

    protected int evaluateByMaterial() {
        long whitePositions = game.getChessPosition().getPositions(Color.WHITE);
        long blackPositions = game.getChessPosition().getPositions(Color.BLACK);
        return Long.bitCount(whitePositions) - Long.bitCount(blackPositions);
    }

}
