package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.TranspositionTableAbstract;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableRoot extends TranspositionTableAbstract {

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    protected boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth) {
        return false;
    }
}
