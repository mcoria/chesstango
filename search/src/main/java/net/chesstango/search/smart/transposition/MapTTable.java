package net.chesstango.search.smart.transposition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MapTTable implements TTable {

    private Map<Long, TranspositionEntry> table = new HashMap<>();

    @Override
    public TranspositionEntry read(long hash) {
        return table.get(hash);
    }

    @Override
    public TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        TranspositionEntry entry = table.computeIfAbsent(hash, key -> new TranspositionEntry());
        entry.hash = hash;
        entry.searchDepth = searchDepth;
        entry.movesAndValue = movesAndValue;
        entry.transpositionBound = bound;
        return entry;
    }

    @Override
    public void clear() {
        table.clear();
    }
}
