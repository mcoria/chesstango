package net.chesstango.search.smart.transposition;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MapTTable implements TTable{

    private Map<Long, Transposition> table = new HashMap<>();

    @Override
    public Transposition get(long hash) {
        return table.get(hash);
    }

    @Override
    public void put(long hash, Transposition entry) {
        table.put(hash, entry);
    }

    @Override
    public void clear() {
        table.clear();
    }
}
