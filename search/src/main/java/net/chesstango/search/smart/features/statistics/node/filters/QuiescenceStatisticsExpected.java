package net.chesstango.search.smart.features.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class QuiescenceStatisticsExpected implements AlphaBetaFilter {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private int[] expectedNodesCounters;

    @Setter
    private Game game;

    @Setter
    private int maxPly;

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
        final int qLevel = currentPly - maxPly;
        int expectedMoves = 0;
        if(game.getStatus().isCheck()){
            expectedMoves = game.getPossibleMoves().size();
        } else {
            for (Move move : game.getPossibleMoves()) {
                if (!move.isQuiet()) {
                    expectedMoves++;
                }
            }
        }
        expectedNodesCounters[qLevel] += expectedMoves;
    }
}
