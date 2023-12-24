package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public class DebugStandingPat implements AlphaBetaFilter {
    @Setter
    @Getter
    private GameEvaluator gameEvaluator;

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return printStandingPa(currentPly, alpha, beta, next::maximize);
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return printStandingPa(currentPly, alpha, beta, next::minimize);
    }

    private long printStandingPa(int currentPly, int alpha, int beta, AlphaBetaFunction fn) {
        int maxValue = gameEvaluator.evaluate();

        System.out.printf(" SP=%d", maxValue);

        return fn.search(currentPly, alpha, beta);
    }
}
