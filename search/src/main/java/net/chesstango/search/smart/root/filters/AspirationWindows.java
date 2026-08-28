package net.chesstango.search.smart.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Setter
public class AspirationWindows implements AlphaBetaFilter, Acceptor, SearchListener {

    static final int OFFSET = 64;

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
            alphaBound = lastBestValue - diffBound(alpha, lastBestValue, 0);
            betaBound = lastBestValue + diffBound(beta, lastBestValue, 0);
        }

        boolean search = true;
        int bestValue;

        int alphaCycle = 1;
        int betaCycle = 1;

        try {
            do {
                listenerMediator.triggerBeforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle++);

                bestValue = next.alphaBeta(currentPly, alphaBound, betaBound);

                listenerMediator.triggerAfterSearchByWindows(false);

                if (bestValue <= alphaBound) {
                    if (alpha < bestValue) {
                        alphaBound = bestValue - diffBound(alpha, bestValue, alphaCycle++);
                    } else {
                        search = false;
                    }
                } else if (betaBound <= bestValue) {
                    if (bestValue < beta) {
                        betaBound = bestValue + diffBound(beta, bestValue, betaCycle++);
                    } else {
                        search = false;
                    }
                } else {
                    search = false;
                }

            } while (search);

            return bestValue;

        } catch (StopSearchingException stopSearchingException) {
            listenerMediator.triggerAfterSearchByWindows(true);
            throw stopSearchingException;
        }
    }

    int diffBound(int maxBound, int currentBound, int cycle) {
        return Math.min(OFFSET << cycle, Math.abs(Math.abs(maxBound) - Math.abs(currentBound)));
    }
}
