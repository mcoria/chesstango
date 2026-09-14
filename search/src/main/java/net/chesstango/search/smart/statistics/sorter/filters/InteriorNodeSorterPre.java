package net.chesstango.search.smart.statistics.sorter.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.statistics.sorter.SorterCounters;

/**
 * @author Mauricio Coria
 */
public class InteriorNodeSorterPre implements AlphaBetaFilter, Acceptor {
    @Getter
    @Setter
    private AlphaBetaFilter next;

    private SorterCounters sorterCounters;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        return next.alphaBeta(currentPly, alpha, beta);
    }
}
