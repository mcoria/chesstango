package net.chesstango.search.smart.features.transposition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class TTableMap implements TTable {

    private final Map<Long, TranspositionEntry> table = new HashMap<>();

    @Override
    public TranspositionEntry read(long hash) {
        return table.get(hash);
    }

    @Override
    public TranspositionEntry write(long hash, TranspositionBound bound, int draft, short move, int value) {
        TranspositionEntry entry = table.computeIfAbsent(hash, key -> new TranspositionEntry());
        entry.hash = hash;
        entry.draft = draft;
        entry.move = move;
        entry.value = value;
        entry.transpositionBound = bound;
        return entry;
    }

    @Override
    public void clear() {
        table.clear();
    }
}
