package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class AlphaBetaEvaluation implements AlphaBetaFilter {
    private Evaluator evaluator;

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(evaluator.evaluate());
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(evaluator.evaluate());
    }
}
