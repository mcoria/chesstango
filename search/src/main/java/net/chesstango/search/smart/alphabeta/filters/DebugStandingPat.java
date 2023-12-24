package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class DebugStandingPat implements AlphaBetaFilter, SearchByCycleListener {
    @Setter
    @Getter
    private GameEvaluator gameEvaluator;

    @Setter
    @Getter
    private AlphaBetaFilter next;

    private PrintStream debugOut;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.debugOut = context.getDebugOut();
    }

    @Override
    public void afterSearch() {

    }

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

        debugOut.printf(" SP=%d", maxValue);

        return fn.search(currentPly, alpha, beta);
    }
}
