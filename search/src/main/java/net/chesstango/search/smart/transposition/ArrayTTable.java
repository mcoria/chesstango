package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public class ArrayTTable implements TTable {

    private static final int ARRAY_SIZE = 1024 * 512;
    private final TranspositionEntry[] transpositionArray;
    private final int[] sessionArray;
    private int currentSessionId;

    public ArrayTTable() {
        this.transpositionArray = new TranspositionEntry[ARRAY_SIZE];
        this.sessionArray = new int[ARRAY_SIZE];
        this.currentSessionId = 1;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.transpositionArray[i] = new TranspositionEntry();
        }
    }

    @Override
    public TranspositionEntry read(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        TranspositionEntry entry = transpositionArray[idx];

        if (sessionArray[idx] != currentSessionId || !entry.isStored(hash)) {
            return null;
        }

        return entry;
    }

    @Override
    public TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        TranspositionEntry entry = getForWrite(hash);
        entry.hash = hash;
        entry.searchDepth = searchDepth;
        entry.movesAndValue = movesAndValue;
        entry.transpositionBound = bound;
        return entry;
    }

    private TranspositionEntry getForWrite(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        TranspositionEntry entry = transpositionArray[idx];

        if (sessionArray[idx] != currentSessionId) {
            entry.reset();
            sessionArray[idx] = currentSessionId;
        }

        return entry;
    }

    @Override
    public void clear() {
        currentSessionId++;
    }
}
