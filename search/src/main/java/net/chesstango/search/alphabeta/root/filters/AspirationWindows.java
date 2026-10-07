package net.chesstango.search.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.*;
import net.chesstango.search.alphabeta.AlphaBetaFilter;


import java.util.Objects;

/**
 * Aspiration windows around the evaluation of the previous iteration.
 * <p>
 * - The first window is [last - OFFSET, last + OFFSET], clamped to the outer (alpha, beta).
 * - On a fail-low only alpha is widened; on a fail-high only beta is widened (the other bound is untouched).
 * - The failing bound grows exponentially (OFFSET << cycle) from the value returned by the failed search,
 * and is clamped to the outer bound, so the last re-search is always a full-width one on that side.
 *
 * @author Mauricio Coria
 */
@Setter
public class AspirationWindows implements AlphaBetaFilter, Acceptor, SearchListener {

    static final int OFFSET = 512;

    /**
     * OFFSET << MAX_SHIFT = 2^30, the largest value that does not overflow an int.
     */
    static final int MAX_SHIFT = 22;

    @Getter
    private AlphaBetaFilter next;

    private ListenerMediator listenerMediator;

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
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        int alphaBound = alpha;
        int betaBound = beta;
        int searchByWindowsCycle = 0;

        if (Objects.nonNull(lastRootMoveEvaluation)) {
            int lastBestValue = lastRootMoveEvaluation.evaluation();
            alphaBound = lowerBound(alpha, lastBestValue, 0);
            betaBound = upperBound(beta, lastBestValue, 0);
        }

        int alphaCycle = 1;
        int betaCycle = 1;
        int bestValue;
        boolean search;

        try {
            do {
                listenerMediator.triggerBeforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle++);

                bestValue = next.alphaBeta(currentPly, alphaBound, betaBound);

                listenerMediator.triggerAfterSearchByWindows(false);

                search = false;

                if (bestValue <= alphaBound && alpha < alphaBound) {
                    // Fail-low: widen alpha only. Strictly decreases alphaBound until it reaches alpha.
                    alphaBound = lowerBound(alpha, bestValue, alphaCycle++);
                    search = true;
                } else if (betaBound <= bestValue && betaBound < beta) {
                    // Fail-high: widen beta only. Strictly increases betaBound until it reaches beta.
                    betaBound = upperBound(beta, bestValue, betaCycle++);
                    search = true;
                }

            } while (search);

            return bestValue;

        } catch (StopSearchingException stopSearchingException) {
            listenerMediator.triggerAfterSearchByWindows(true);
            throw stopSearchingException;
        }
    }

    int lowerBound(int alpha, int center, int cycle) {
        return (int) Math.max(alpha, (long) center - delta(cycle));
    }

    int upperBound(int beta, int center, int cycle) {
        return (int) Math.min(beta, (long) center + delta(cycle));
    }

    int delta(int cycle) {
        return (OFFSET << Math.min(cycle, MAX_SHIFT)) - 1;
    }
}