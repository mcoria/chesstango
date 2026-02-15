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
    public InsertResult save(TranspositionEntry entry) {
        TranspositionEntry storedEntry = table.computeIfAbsent(entry.hash, key -> new TranspositionEntry());
        InsertResult result = storedEntry.hash == entry.hash ? InsertResult.UPDATED : InsertResult.INSERTED;
        storedEntry.hash = entry.hash;
        storedEntry.draft = entry.draft;
        storedEntry.move = entry.move;
        storedEntry.value = entry.value;
        storedEntry.bound = entry.bound;
        return result;
    }

    @Override
    public void clear() {
        table.clear();
    }
}
