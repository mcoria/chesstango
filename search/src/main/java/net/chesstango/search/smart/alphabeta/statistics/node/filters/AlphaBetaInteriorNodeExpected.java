package net.chesstango.search.smart.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBetaInteriorNodeExpected implements AlphaBetaFilter, Acceptor {

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
        updateCounters(currentPly);
        return next.alphaBeta(currentPly, alpha, beta);
    }

    protected void updateCounters(final int currentPly) {
        nodeCounters.increaseExpectedCounter(currentPly + 1, game.getPossibleMoves().size());
    }
}

