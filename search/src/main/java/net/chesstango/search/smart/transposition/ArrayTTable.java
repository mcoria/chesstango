package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public class ArrayTTable implements TTable {

    private final static int TABLE_SIZE = 1024 * 128 ;

    private final TranspositionEntry[] table;
    private final long[] hashTable;

    public ArrayTTable() {
        table = new TranspositionEntry[TABLE_SIZE];
        hashTable = new long[TABLE_SIZE];

        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new TranspositionEntry();
        }
    }

    @Override
    public boolean read(long hash, TranspositionEntry entry) {
        int idx = Math.abs((int) (hash % TABLE_SIZE));

        if (hashTable[idx] != hash) {
            return false;
        }

        TranspositionEntry refEntry = table[idx];

        entry.loadValues(refEntry);

        return true;
    }

    @Override
    public void write(long hash, TranspositionEntry entry) {
        int idx = Math.abs((int) (hash % TABLE_SIZE));

        hashTable[idx] = hash;

        table[idx].loadValues(entry);
    }
}
