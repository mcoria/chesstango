package net.chesstango.search.smart.alphabeta.egtb.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

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
    public int maximize(int currentPly, int alpha, int beta) {
        return endGameTableBase.evaluate();
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        return endGameTableBase.evaluate();
    }
}
