package net.chesstango.search.smart.alphabeta.transposition;

/**
 * @author Mauricio Coria
 */
public class TTableArrayPrimitives implements TTable {

    private final static byte EXACT = ((byte) 0b00000001);
    private final static byte LOWER_BOUND = ((byte) 0b00000010);
    private final static byte UPPER_BOUND = ((byte) 0b00000011);
    private final static int ARRAY_SIZE = 1024 * 512;

    /*
        Data layout:
         - byte[0] = TranspositionBound
         - byte[1] = draft (with sign)
         - byte[2-3] = move
         - byte[4-8] = value
     */

    private final int[] sessionArray;
    private final long[] hashArray;
    private final long[] dataArray;
    private int currentSessionId;

    public TTableArrayPrimitives() {
        this.sessionArray = new int[ARRAY_SIZE];
        this.hashArray = new long[ARRAY_SIZE];
        this.dataArray = new long[ARRAY_SIZE];
        this.currentSessionId = Integer.MIN_VALUE + 1;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sessionArray[i] = Integer.MIN_VALUE;
        }
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        if (sessionArray[idx] != currentSessionId || hashArray[idx] != hash) {
            return false;
        }

        long data = dataArray[idx];

        // Extract fields from the data
        byte boundByte = (byte) ((data >>> 56) & 0xFF);
        byte draftByte = (byte) ((data >>> 48) & 0xFF);
        short move = (short) ((data >>> 32) & 0xFFFF);
        int value = (int) (data & 0xFFFFFFFFL);


        // Copy stored entry fields to the output entry
        entry.hash = hash;
        entry.draft = draftByte;
        entry.move = move;
        entry.value = value;
        entry.bound = boundByte == EXACT ? TranspositionBound.EXACT : boundByte == LOWER_BOUND ? TranspositionBound.LOWER_BOUND : TranspositionBound.UPPER_BOUND;

        return true;
    }

    @Override
    public SaveResult save(TranspositionEntry entry) {
        int idx = (int) Math.abs(entry.hash % ARRAY_SIZE);

        SaveResult result;
        if (sessionArray[idx] != currentSessionId) {
            sessionArray[idx] = currentSessionId;
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

        byte boundByte = entry.bound == TranspositionBound.EXACT ? EXACT : entry.bound == TranspositionBound.LOWER_BOUND ? LOWER_BOUND : UPPER_BOUND;
        byte draftByte = (byte) (entry.draft);
        short move = entry.move;
        int value = entry.value;

        dataArray[idx] = ((boundByte & 0xFFL) << 56) | ((draftByte & 0xFFL) << 48) | ((move & 0xFFFFL) << 32) | (value & 0xFFFFFFFFL);

        return result;
    }


    @Override
    public void clear() {
        if (currentSessionId < Integer.MAX_VALUE) {
            currentSessionId++;
        } else {
            currentSessionId = Integer.MIN_VALUE + 1;
        }
    }
}
