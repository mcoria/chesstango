package net.chesstango.search.smart.alphabeta.transposition;

import static net.chesstango.search.Bound.*;

/**
 * @author Mauricio Coria
 */
public class TTableArrayPrimitives implements TTable {

    static final long EXACT_BOUND_VALUE = 0b00000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long LOWER_BOUND_VALUE = 0b00000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long UPPER_BOUND_VALUE = 0b00000011_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;


    static final long AGE_MASK = 0b11111100_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long BOUND_MASK = 0b00000011_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long DRAFT_MASK = 0b00000000_11111111_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long MOVE_MASK = 0b00000000_00000000_11111111_11111111_00000000_00000000_00000000_00000000L;
    static final long VALUE_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;


    static final int ARRAY_SIZE = 2 * 1024 * 512;

    static final int STALE_AGE = 3;

    static final int MAX_AGE = 0x3F;

    /*
        Data layout:
         - byte[0] = age AND TranspositionBound
         - byte[1] = draft (with sign)
         - byte[2-3] = move
         - byte[4-8] = value
     */

    final long[] hashArray;
    final long[] dataArray;
    int currentAge;

    public TTableArrayPrimitives() {
        this.hashArray = new long[ARRAY_SIZE];
        this.dataArray = new long[ARRAY_SIZE];
        this.currentAge = STALE_AGE;
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        if (hashArray[idx] != hash) {
            return false;
        }

        long data = dataArray[idx];

        // Extract fields from the data
        int age = (int) ((data & AGE_MASK) >>> 58);

        if (currentAge < age || currentAge - age > STALE_AGE) {
            return false;
        }

        long bound = data & BOUND_MASK;
        byte draftByte = (byte) ((data & DRAFT_MASK) >>> 48);
        short move = (short) ((data & MOVE_MASK) >>> 32);
        int value = (int) (data & VALUE_MASK);

        // Copy stored entry fields to the output entry
        entry.hash = hash;
        entry.draft = draftByte;
        entry.move = move;
        entry.value = value;
        entry.bound = bound == EXACT_BOUND_VALUE ? EXACT : bound == LOWER_BOUND_VALUE ? LOWER_BOUND : UPPER_BOUND;

        return true;
    }

    @Override
    public SaveResult save(TranspositionEntry entry) {
        int idx = (int) Math.abs(entry.hash % ARRAY_SIZE);

        long data = dataArray[idx];

        // Extract fields from the data
        int age = (int) ((data & AGE_MASK) >>> 58);

        SaveResult result;
        if (age != currentAge) {
            hashArray[idx] = entry.hash;
            result = SaveResult.INSERTED;
        } else {
            if (hashArray[idx] == entry.hash) {
                result = SaveResult.UPDATED;
            } else {
                hashArray[idx] = entry.hash;
                result = SaveResult.OVER_WRITTEN;
            }
        }

        long bound = entry.bound == EXACT ? EXACT_BOUND_VALUE : entry.bound == LOWER_BOUND ? LOWER_BOUND_VALUE : UPPER_BOUND_VALUE;
        byte draftByte = entry.draft;
        short move = entry.move;
        int value = entry.value;

        dataArray[idx] = ((currentAge & 0x3FL) << 58) | bound | ((draftByte & 0xFFL) << 48) | ((move & 0xFFFFL) << 32) | (value & 0xFFFFFFFFL);

        return result;
    }

    @Override
    public void increaseAge() {
        if (currentAge < MAX_AGE) {
            currentAge++;
        } else {
            clear();
            currentAge++;
        }
    }


    @Override
    public void clear() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            hashArray[i] = 0;
            dataArray[i] = 0;
        }
        currentAge = STALE_AGE;
    }

    public int getFillPercentage() {
        int filled = 0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            long data = dataArray[i];
            int age = (int) ((data & AGE_MASK) >>> 58);
            if (!(currentAge < age || currentAge - age > STALE_AGE)) {
                filled++;
            }
        }
        return (filled * 100 / ARRAY_SIZE);
    }
}
