package net.chesstango.search.smart.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBetaTerminalNodeStatistics implements AlphaBetaFilter {

    @Getter
    private AlphaBetaFilter next;

    private long[] visitedNodesCounters;

    private long[] visitedNodesCountersQuiescence;

    private int depth;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        updateCounters(currentPly);
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        updateCounters(currentPly);
        return next.minimize(currentPly, alpha, beta);
    }

    protected void updateCounters(final int currentPly) {
        if (currentPly > 0) {
            if (currentPly < depth) {
                visitedNodesCounters[currentPly - 1]++;
            } else if (currentPly > depth) {
                final int qLevel = currentPly - depth - 1;
                //visitedNodesCountersQuiescence[qLevel]++;
            }
        }
    }
}

