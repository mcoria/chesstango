package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class LoopEvaluation implements AlphaBetaFilter {
    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(null, 0);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(null, 0);
    }
}
