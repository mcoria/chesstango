package net.chesstango.search.smart.alphabeta.transposition.filters;

import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableQ extends TranspositionTableAbstract {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    protected boolean isTranspositionEntryValid(int draft) {
        return true;
    }

}
