package net.chesstango.search.smart.alphabeta.transposition.filters;

import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable extends TranspositionTableAbstract implements Acceptor {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected boolean isTranspositionEntryValid(int draft) {
        return draft <= entryWorkspace.getDraft();
    }

}
