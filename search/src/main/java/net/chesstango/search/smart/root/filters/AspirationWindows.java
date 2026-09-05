package net.chesstango.search.smart.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.*;
import net.chesstango.search.smart.AlphaBetaFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Setter
public class AspirationWindows implements AlphaBetaFilter, Acceptor, SearchListener, SearchByDepthListener {

    @Getter
    private AlphaBetaFilter next;

    protected int depth;

    private ListenerMediator listenerMediator;

    private List<RootMoveEvaluation> rootMoveEvaluations;

    private int lastDepthEvaluation;

    private int windows;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        rootMoveEvaluations = new ArrayList<>();
    }

    @Override
    public void beforeSearchByDepth() {
        if (depth >= 3) {
            lastDepthEvaluation = rootMoveEvaluations.getLast().evaluation();
            windows = Math.max(standardDeviation(), 100);
        }
    }

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        if (depth < 3) {
            return next.alphaBeta(currentPly, alpha, beta);
        } else {
            return imp(currentPly, alpha, beta);
        }
    }

    public void addRootMoveEvaluation(RootMoveEvaluation bestRootMoveEvaluation) {
        rootMoveEvaluations.add(bestRootMoveEvaluation);
    }

    int imp(final int currentPly, final int alpha, final int beta) {
        int alphaBound = calculateAlphaBound(alpha, lastDepthEvaluation);
        int betaBound = calculateBetaBound(beta, lastDepthEvaluation);

        int searchByWindowsCycle = 0;

        try {
            do {
                listenerMediator.triggerBeforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle++);

                int currentValue = next.alphaBeta(currentPly, alphaBound, betaBound);

                listenerMediator.triggerAfterSearchByWindows(false);

                if (currentValue <= alpha) {
                    return currentValue;
                } else if (currentValue <= alphaBound) {
                    alphaBound = calculateAlphaBound(alpha, currentValue);
                } else if (beta <= currentValue) {
                    return currentValue;
                } else if (betaBound <= currentValue) {
                    betaBound = calculateBetaBound(beta, currentValue);
                } else {
                    return currentValue;
                }

            } while (true);

        } catch (StopSearchingException stopSearchingException) {
            listenerMediator.triggerAfterSearchByWindows(true);
            throw stopSearchingException;
        }
    }

    int calculateBetaBound(int beta, int currentValue) {
        int i = 0;
        long betaBound;

        do {
            betaBound = lastDepthEvaluation + (long) windows * Math.powExact(2, i++);
        } while (betaBound <= currentValue && betaBound < beta);

        if (beta <= betaBound) {
            return beta;
        }

        return Math.toIntExact(betaBound);
    }

    int calculateAlphaBound(final int alpha, final int currentValue) {
        int i = 0;
        long alphaBound;

        do {
            alphaBound = lastDepthEvaluation - (long) windows * Math.powExact(2, i++);
        } while (currentValue <= alphaBound && alpha < alphaBound);

        if (alphaBound <= alpha) {
            return alpha;
        }

        return Math.toIntExact(alphaBound);
    }

    int standardDeviation() {
        // 1. Calculate the mean (average)
        double mean = rootMoveEvaluations
                .stream()
                .mapToInt(RootMoveEvaluation::evaluation)
                .average()
                .orElse(0.0);

        // 2. Calculate the sum of squared differences from the mean
        double varianceSum = rootMoveEvaluations
                .stream()
                .mapToInt(RootMoveEvaluation::evaluation)
                .mapToDouble(num -> Math.pow(num - mean, 2))
                .sum();

        // 3. Divide by the total elements and take the square root
        return (int) Math.sqrt(varianceSum / rootMoveEvaluations.size());
    }
}
