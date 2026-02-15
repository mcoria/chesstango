package net.chesstango.search.smart.features.transposition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class TTableMap implements TTable {

    private final Map<Long, TranspositionEntry> table = new HashMap<>();

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        TranspositionEntry storedEntry = table.get(hash);

        if (storedEntry == null) {
            return false;
        }

        // Copy stored entry fields to the output entry
        entry.hash = storedEntry.hash;
        entry.draft = storedEntry.draft;
        entry.move = storedEntry.move;
        entry.value = storedEntry.value;
        entry.bound = storedEntry.bound;

        return true;
    }

    @Override
    public TranspositionEntry write(long hash, TranspositionBound bound, int draft, short move, int value) {
        TranspositionEntry entry = table.computeIfAbsent(hash, key -> new TranspositionEntry());
        entry.hash = hash;
        entry.draft = draft;
        entry.move = move;
        entry.value = value;
        entry.bound = bound;
        return entry;
    }

    @Override
    public void clear() {
        table.clear();
    }
}
