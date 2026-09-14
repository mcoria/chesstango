package net.chesstango.search.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.statistics.node.NodeCounters;

/**
 * @author Mauricio Coria
 */
@Setter
public class InteriorNodeExpected implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    private NodeCounters nodeCounters;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        increaseExpectedCounter(currentPly);
        return next.alphaBeta(currentPly, alpha, beta);
    }

    protected void increaseExpectedCounter(final int currentPly) {
        nodeCounters.increaseExpectedCounter(currentPly + 1, game.getPossibleMoves().size());
    }
}

