package net.chesstango.search.smart.features.transposition;

/**
 * @author Mauricio Coria
 */
public class TTableArray implements TTable {

    private static final int ARRAY_SIZE = 1024 * 512;
    private final TranspositionEntry[] transpositionArray;
    private final int[] sessionArray;
    private int currentSessionId;

    public TTableArray() {
        this.transpositionArray = new TranspositionEntry[ARRAY_SIZE];
        this.sessionArray = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.transpositionArray[i] = new TranspositionEntry();
            this.sessionArray[i] = Integer.MIN_VALUE;
        }
        this.currentSessionId = Integer.MIN_VALUE + 1;
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        TranspositionEntry storedEntry = transpositionArray[idx];

        if (sessionArray[idx] != currentSessionId || !storedEntry.isStored(hash)) {
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
        TranspositionEntry entry = getForWrite(hash);
        entry.hash = hash;
        entry.draft = draft;
        entry.move = move;
        entry.value = value;
        entry.bound = bound;
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
        if (currentSessionId < Integer.MAX_VALUE) {
            currentSessionId++;
        } else {
            currentSessionId = Integer.MIN_VALUE;
        }
    }
}
