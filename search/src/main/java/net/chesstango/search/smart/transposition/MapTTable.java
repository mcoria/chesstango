package net.chesstango.search.smart.transposition;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MapTTable<T extends TranspositionEntry> implements TTable<T>{

    private Map<Long, T> table = new HashMap<>();

    @Override
    public T get(long hash) {
        return table.get(hash);
    }

    @Override
    public void put(long hash, T entry) {
        table.put(hash, entry);
    }

    @Override
    public void clear() {
        table.clear();
    }
}
