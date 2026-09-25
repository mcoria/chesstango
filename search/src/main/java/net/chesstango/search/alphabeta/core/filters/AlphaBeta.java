package net.chesstango.search.alphabeta.core.filters;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBeta extends AlphaBetaAbstract implements AlphaBetaFilter, Acceptor {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
