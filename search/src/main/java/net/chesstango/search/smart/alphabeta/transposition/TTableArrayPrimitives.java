package net.chesstango.search.smart.alphabeta.transposition;

/**
 * @author Mauricio Coria
 */
public class TTableArrayPrimitives implements TTable {

    private final static long EXACT_BOUND = 0b00000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    private final static long LOWER_BOUND = 0b00000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    private final static long UPPER_BOUND = 0b00000011_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    private final static long BOUND_MASK = 0b00000011_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;

    private final static int ARRAY_SIZE = 2 * 1024 * 512;

    private static final int STALE_AGE = 5;

    /*
        Data layout:
         - byte[0] = age AND TranspositionBound
         - byte[1] = draft (with sign)
         - byte[2-3] = move
         - byte[4-8] = value
     */

    private final long[] hashArray;
    private final long[] dataArray;
    private int currentAge;

    public TTableArrayPrimitives() {
        this.hashArray = new long[ARRAY_SIZE];
        this.dataArray = new long[ARRAY_SIZE];
        this.currentAge = 1;
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        if (hashArray[idx] != hash) {
            return false;
        }

        long data = dataArray[idx];

        // Extract fields from the data
        int age = (int) (data >>> 58);

        if (currentAge - age > STALE_AGE) {
            return false;
        }

        long bound = data & BOUND_MASK;
        byte draftByte = (byte) ((data >>> 48) & 0xFF);
        short move = (short) ((data >>> 32) & 0xFFFF);
        int value = (int) (data & 0xFFFFFFFFL);

        // Copy stored entry fields to the output entry
        entry.hash = hash;
        entry.draft = draftByte;
        entry.move = move;
        entry.value = value;
        entry.bound = bound == EXACT_BOUND ? TranspositionBound.EXACT : bound == LOWER_BOUND ? TranspositionBound.LOWER_BOUND : TranspositionBound.UPPER_BOUND;

        return true;
    }

    @Override
    public SaveResult save(TranspositionEntry entry) {
        int idx = (int) Math.abs(entry.hash % ARRAY_SIZE);

        long data = dataArray[idx];

        // Extract fields from the data
        int age = (int) (data >>> 58);

        SaveResult result;
        if (age != currentAge) {
            hashArray[idx] = entry.hash;
            result = SaveResult.INSERTED;
        } else {
            if (hashArray[idx] == entry.hash) {
                result = SaveResult.UPDATED;
            } else {
                result = SaveResult.OVER_WRITTEN;
                hashArray[idx] = entry.hash;
            }
        }

        long bound = entry.bound == TranspositionBound.EXACT ? EXACT_BOUND : entry.bound == TranspositionBound.LOWER_BOUND ? LOWER_BOUND : UPPER_BOUND;
        byte draftByte = entry.draft;
        short move = entry.move;
        int value = entry.value;

        dataArray[idx] = ((currentAge & 0x3FL) << 58) | bound | ((draftByte & 0xFFL) << 48) | ((move & 0xFFFFL) << 32) | (value & 0xFFFFFFFFL);

        return result;
    }

    @Override
    public void increaseAge() {
        if (currentAge < 0x3F) {
            currentAge++;
        } else {
            currentAge = 0;
        }
    }


    @Override
    public void clear() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            hashArray[i] = 0;
            dataArray[i] = 0;
        }
    }
}
