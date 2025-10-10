package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Setter
public class AspirationWindows implements AlphaBetaFilter {

    @Getter
    private AlphaBetaFilter next;

    private SearchListenerMediator searchListenerMediator;

    private Integer lastBestValue;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize);
    }

    private long process(int currentPly, final int alpha, final int beta, AlphaBetaFunction fn) {
        int alphaBound = alpha;
        int betaBound = beta;
        int searchByWindowsCycle = 0;

        if (Objects.nonNull(lastBestValue)) {
            alphaBound = lastBestValue - diffBound(alpha, lastBestValue, 0);
            betaBound = lastBestValue + diffBound(beta, lastBestValue, 0);
        }

        boolean search = true;
        long bestMoveAndValue;
        int bestValue;

        int alphaCycle = 1;
        int betaCycle = 1;
        do {
            searchListenerMediator.triggerBeforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle++);

            bestMoveAndValue = fn.search(currentPly, alphaBound, betaBound);

            bestValue = TranspositionEntry.decodeValue(bestMoveAndValue);

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

        return bestMoveAndValue;
    }

    private static final int OFFSET = 64;

    protected int diffBound(int maxBound, int currentBound, int cycle) {
        return Math.min(OFFSET << cycle, Math.abs(Math.abs(maxBound) - Math.abs(currentBound)));
    }
}
