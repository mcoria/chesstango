package net.chesstango.search.smart.alphabeta.transposition;

/**
 * @author Mauricio Coria
 */
public class TTableArrayObj implements TTable {

    private static final int ARRAY_SIZE = 1024 * 512;
    private final TranspositionEntry[] transpositionArray;
    private final int[] ageArray;
    private int currentAge;

    public TTableArrayObj() {
        this.transpositionArray = new TranspositionEntry[ARRAY_SIZE];
        this.ageArray = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.transpositionArray[i] = new TranspositionEntry();
            this.ageArray[i] = Integer.MIN_VALUE;
        }
        this.currentAge = Integer.MIN_VALUE + 1;
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        TranspositionEntry storedEntry = transpositionArray[idx];

        if (ageArray[idx] != currentAge || storedEntry.hash != hash) {
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
    public SaveResult save(TranspositionEntry entry) {
        int idx = (int) Math.abs(entry.hash % ARRAY_SIZE);

        TranspositionEntry storedEntry = transpositionArray[idx];

        SaveResult result;
        if (ageArray[idx] != currentAge) {
            ageArray[idx] = currentAge;
            result = SaveResult.INSERTED;
        } else {
            if (storedEntry.hash == entry.hash) {
                result = SaveResult.UPDATED;
            } else {
                result = SaveResult.OVER_WRITTEN;
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
    public void increaseAge() {
        if (currentAge < Integer.MAX_VALUE) {
            currentAge++;
        } else {
            currentAge = Integer.MIN_VALUE;
        }
    }


    @Override
    public void clear() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            transpositionArray[i] = null;
            ageArray[i] = 0;
        }
    }
}
