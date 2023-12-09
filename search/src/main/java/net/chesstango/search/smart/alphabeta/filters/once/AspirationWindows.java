package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class AspirationWindows implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {
    @Setter
    private AlphaBetaFilter next;

    @Setter
    private MoveEvaluationTracker moveEvaluationTracker;

    private Integer lastBestValue;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        lastBestValue = null;
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        lastBestValue = context.getLastBestEvaluation();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {

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

        //System.out.printf("MaxPly = %d alphaCycle=%d betaCycle=%d\n", maxPly, alphaCycle, betaCycle);
        int alphaCycle = 1;
        int betaCycle = 1;
        do {

            moveEvaluationTracker.beforeSearchByWindows(alphaBound, betaBound);

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

            moveEvaluationTracker.afterSearchByWindows(!search);
        } while (search);

        //System.out.printf("\n\n");

        return bestMoveAndValue;
    }

    private static final int OFFSET = 64;

    protected int diffBound(int maxBound, int currentBound, int cycle) {
        return Math.min(OFFSET << cycle, Math.abs(Math.abs(maxBound) - Math.abs(currentBound)));
    }
}
