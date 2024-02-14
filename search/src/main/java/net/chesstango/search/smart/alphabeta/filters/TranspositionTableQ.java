package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableQ extends TranspositionTableAbstract {

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        super.beforeSearch(context);
        this.maxMap = context.getQMaxMap();
        this.minMap = context.getQMinMap();
    }


    @Override
    protected boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth) {
        return true;
    }

}
