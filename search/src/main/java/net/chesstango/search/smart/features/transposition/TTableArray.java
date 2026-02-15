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

        if (sessionArray[idx] != currentSessionId || storedEntry.hash != hash) {
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
        int idx = (int) Math.abs(entry.hash % ARRAY_SIZE);

        TranspositionEntry storedEntry = transpositionArray[idx];

        InsertResult result;
        if (sessionArray[idx] != currentSessionId) {
            sessionArray[idx] = currentSessionId;
            result = InsertResult.INSERTED;
        } else {
            if (storedEntry.hash == entry.hash) {
                result = InsertResult.UPDATED;
            } else {
                result = InsertResult.REPLACED;
            }
        }

        storedEntry.hash = entry.hash;
        storedEntry.draft = entry.draft;
        storedEntry.move = entry.move;
        storedEntry.value = entry.value;
        storedEntry.bound = entry.bound;

        return result;
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
