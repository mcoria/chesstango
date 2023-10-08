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

    private static final int OFFSET = 64;

    private long process(int currentPly, final int alpha, final int beta, AlphaBetaFunction fn) {
        int alphaBound = alpha;
        int betaBound = beta;

        if (Objects.nonNull(lastBestValue)) {
            alphaBound = lastBestValue - Math.min(OFFSET, Math.abs(Math.abs(alpha) - Math.abs(lastBestValue)));
            betaBound = lastBestValue + Math.min(OFFSET, Math.abs(Math.abs(beta) - Math.abs(lastBestValue)));
        }

        boolean search = true;
        long bestMoveAndValue;
        int bestValue;
        int bestValueOffset;

        int alphaCycle = 1;
        int betaCycle = 1;

        do {
            bestMoveAndValue = fn.search(currentPly, alphaBound, betaBound);

            bestValue = TranspositionEntry.decodeValue(bestMoveAndValue);

            if (bestValue <= alphaBound) {
                bestValueOffset = Math.min(OFFSET << alphaCycle, Math.abs(Math.abs(alpha) - Math.abs(bestValue)));
                alphaBound = bestValue - bestValueOffset;
                alphaCycle++;
            } else if (betaBound <= bestValue) {
                bestValueOffset = Math.min(OFFSET << betaCycle, Math.abs(Math.abs(beta) - Math.abs(bestValue)));
                betaBound = bestValue + bestValueOffset;
                betaCycle++;
            } else {
                search = false;
            }
        } while (search);

        return bestMoveAndValue;
    }

    protected int diffBound(int bestValue, int maxBound, int cycle) {
        int absDiff = 2 << cycle;
        return 0;
    }

}
