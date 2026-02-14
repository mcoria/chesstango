package net.chesstango.search.smart.features.transposition.filters;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableQ extends TranspositionTableAbstract {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    protected boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int draft) {
        return true;
    }

}
