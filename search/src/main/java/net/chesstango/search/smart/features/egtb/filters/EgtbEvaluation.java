package net.chesstango.search.smart.features.egtb.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class EgtbEvaluation implements AlphaBetaFilter, Acceptor {

    private EndGameTableBase endGameTableBase;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(null, 0);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return TranspositionEntry.encode(null, 0);
    }
}
