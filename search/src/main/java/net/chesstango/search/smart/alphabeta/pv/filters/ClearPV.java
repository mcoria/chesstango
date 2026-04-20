package net.chesstango.search.smart.alphabeta.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class ClearPV implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    private TriangularPVTable trianglePV;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        trianglePV.clearPV(currentPly);

        return next.alphaBeta(currentPly, alpha, beta);
    }
}
