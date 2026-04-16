package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Setter
public class AspirationWindows implements AlphaBetaFilter, Acceptor, SearchByCycleListener {

    private static final int OFFSET = 64;

    @Getter
    private AlphaBetaFilter next;

    private SearchListenerMediator searchListenerMediator;
    
    private RootMoveEvaluation lastRootMoveEvaluation;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.lastRootMoveEvaluation = null;
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        int alphaBound = alpha;
        int betaBound = beta;
        int searchByWindowsCycle = 0;

        if (Objects.nonNull(lastRootMoveEvaluation)) {
            int lastBestValue = lastRootMoveEvaluation.evaluation();
            alphaBound = lastBestValue - diffBound(alpha, lastBestValue, 0);
            betaBound = lastBestValue + diffBound(beta, lastBestValue, 0);
        }

        boolean search = true;
        int bestValue;

        int alphaCycle = 1;
        int betaCycle = 1;
        do {
            searchListenerMediator.triggerBeforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle++);

            bestValue = next.alphaBeta(currentPly, alphaBound, betaBound);

            if (bestValue <= alphaBound) {
                if (alpha < bestValue) {
                    alphaBound = bestValue - diffBound(alpha, bestValue, alphaCycle);
                    alphaCycle++;
                } else {
                    search = false;
                }
            } else if (betaBound <= bestValue) {
                if (bestValue < beta) {
                    betaBound = bestValue + diffBound(beta, bestValue, betaCycle);
                    betaCycle++;
                } else {
                    search = false;
                }
            } else {
                search = false;
            }

            searchListenerMediator.triggerAfterSearchByWindows(!search);
        } while (search);

        return bestValue;
    }

    protected int diffBound(int maxBound, int currentBound, int cycle) {
        return Math.min(OFFSET << cycle, Math.abs(Math.abs(maxBound) - Math.abs(currentBound)));
    }
}
