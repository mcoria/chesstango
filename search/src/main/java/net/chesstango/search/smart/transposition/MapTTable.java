package net.chesstango.search.smart.transposition;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
class MapTTable<T extends TranspositionEntry> implements TTable<T>{

    private Map<Long, T> table = new HashMap<>();

    @Override
    public T read(long hash) {
        return table.get(hash);
    }

    @Override
    public void write(long hash, T entry) {
        table.put(entry.getHash(), entry);
    }
}
