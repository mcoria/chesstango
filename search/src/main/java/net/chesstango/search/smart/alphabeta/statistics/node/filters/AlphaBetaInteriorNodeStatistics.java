package net.chesstango.search.smart.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBetaInteriorNodeStatistics implements AlphaBetaFilter {

    @Getter
    private AlphaBetaFilter next;


    private long[] expectedNodesCounters;
    private long[] visitedNodesCounters;

    private long[] expectedNodesCountersQuiescence;
    private long[] visitedNodesCountersQuiescence;


    private Game game;

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
        if (currentPly < depth) {
            expectedNodesCounters[currentPly] += game.getPossibleMoves().size();
        } else {
            final int qLevel = currentPly - depth;
            int expectedMoves = 0;
            for (Move move : game.getPossibleMoves()) {
                if (!move.isQuiet()) {
                    expectedMoves++;
                }
            }
            expectedNodesCountersQuiescence[qLevel] += expectedMoves;
        }

        if (currentPly > 0) {
            if (currentPly < depth) {
                visitedNodesCounters[currentPly - 1]++;
            } else {
                final int qLevel = currentPly - depth;
                visitedNodesCountersQuiescence[qLevel]++;
            }
        }

    }
}

