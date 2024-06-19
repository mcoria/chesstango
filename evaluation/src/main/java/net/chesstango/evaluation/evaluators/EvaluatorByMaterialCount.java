package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;

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
    protected int evaluateByMaterial() {
        long whitePositions = game.getChessPosition().getPositions(Color.WHITE);
        long blackPositions = game.getChessPosition().getPositions(Color.BLACK);
        return Long.bitCount(whitePositions) - Long.bitCount(blackPositions);
    }

}
