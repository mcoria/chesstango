package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableQ extends TranspositionTableAbstract implements SearchByDepthListener {

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getQMaxMap();
        this.minMap = context.getQMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    protected boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth) {
        return entry.isStored(hash);
    }

}
