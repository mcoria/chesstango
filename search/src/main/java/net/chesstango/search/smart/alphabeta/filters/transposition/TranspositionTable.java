package net.chesstango.search.smart.alphabeta.filters.transposition;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable extends TranspositionTableAbstract {


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        super.beforeSearch(context);
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    protected boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth) {
        return searchDepth <= entry.searchDepth;
    }

}
