package net.chesstango.search.alphabeta.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.pv.model.PVTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class PropagatePV implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    private PVTable trianglePV;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        int value = next.alphaBeta(currentPly, alpha, beta);

        if (alpha < value && value < beta) {
            trianglePV.propagatePrincipalVariation(currentPly);
        }

        return value;
    }

}
