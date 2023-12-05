package net.chesstango.search.smart.transposition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MapTTable implements TTable {

    private Map<Long, TranspositionEntry> table = new HashMap<>();

    @Override
    public TranspositionEntry getForWrite(long hash) {
        return table.computeIfAbsent(hash, key -> new TranspositionEntry());
    }

    @Override
    public TranspositionEntry getForRead(long hash) {
        return table.get(hash);
    }

    @Override
    public void clear() {
        table.clear();
    }
}
