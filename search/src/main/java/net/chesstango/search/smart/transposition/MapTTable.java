package net.chesstango.search.smart.transposition;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
class MapTTable implements TTable {

    private Map<Long, TranspositionEntry> table = new HashMap<>();

    @Override
    public boolean read(long hash, TranspositionEntry entry) {
        TranspositionEntry theEntry = table.get(hash);

        if (theEntry == null) {
            return false;
        }

        entry.loadValues(theEntry);

        return true;
    }

    @Override
    public void write(long hash, TranspositionEntry entry) {

        TranspositionEntry theEntry = new TranspositionEntry();

        theEntry.loadValues(entry);

        table.put(hash, theEntry);
    }
}
