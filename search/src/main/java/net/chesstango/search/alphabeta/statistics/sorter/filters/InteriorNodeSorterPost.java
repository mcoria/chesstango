package net.chesstango.search.alphabeta.statistics.sorter.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.statistics.sorter.SorterCounters;

/**
 * @author Mauricio Coria
 */
public class InteriorNodeSorterPost implements AlphaBetaFilter, Acceptor{
    @Getter
    @Setter
    private AlphaBetaFilter next;

    @Setter
    private SorterCounters sorterCounters;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        sorterCounters.increaseIndex(currentPly);
        return next.alphaBeta(currentPly, alpha, beta);
    }
}
