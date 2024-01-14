package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

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
        return Objects.nonNull(entry);
    }

    @Override
    protected int getDepth(int currentPly) {
        return currentPly - maxPly;
    }

}
