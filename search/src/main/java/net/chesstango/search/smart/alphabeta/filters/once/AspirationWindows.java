package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class AspirationWindows implements AlphaBetaFilter {
    @Setter
    private AlphaBetaFilter next;

    private Integer lastBestValue;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        lastBestValue = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        lastBestValue = context.getLastBestValue();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

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
        } while (search);

        return bestMoveAndValue;
    }

    private static final int OFFSET = 64;

    protected int diffBound(int maxBound, int bestValue, int cycle) {
        return Math.min(OFFSET << cycle, Math.abs(Math.abs(maxBound) - Math.abs(bestValue)));
    }
}
