package net.chesstango.search.smart.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBetaRootNodeStatistics implements AlphaBetaFilter {

    @Getter
    private AlphaBetaFilter next;

    private NodeCounters nodeCounters;

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
        assert currentPly == 0;

        nodeCounters.increaseRootCounter();

        nodeCounters.increaseExpectedCounter(0, game.getPossibleMoves().size());
    }
}

