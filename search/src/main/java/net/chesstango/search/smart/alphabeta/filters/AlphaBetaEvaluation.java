package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaEvaluation implements AlphaBetaFilter {
    @Setter
    @Getter
    private GameEvaluator gameEvaluator;

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(gameEvaluator.evaluate());
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(gameEvaluator.evaluate());
    }
}
