package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByMaterialCount extends AbstractEvaluator {
    @Override
    public int evaluateNonFinalStatus() {
        return evaluateByMaterial();
    }


    protected int evaluateByMaterial() {
        long whitePositions = game.getPosition().getPositions(Color.WHITE);
        long blackPositions = game.getPosition().getPositions(Color.BLACK);
        return Long.bitCount(whitePositions) - Long.bitCount(blackPositions);
    }

}
