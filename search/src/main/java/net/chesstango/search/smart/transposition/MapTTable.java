package net.chesstango.search.smart.transposition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MapTTable implements TTable {

    private Map<Long, TranspositionEntry> table = new HashMap<>();

    @Override
    public TranspositionEntry get(long hash) {
        return table.get(hash);
    }

    @Override
    public TranspositionEntry allocate(long hash) {
        return table.computeIfAbsent(hash, key -> new TranspositionEntry());
    }

    @Override
    public void clear() {
        table.clear();
    }
}
