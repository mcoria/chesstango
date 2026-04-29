package net.chesstango.search.smart.alphabeta.transposition;

import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

import static net.chesstango.search.Bound.*;

/**
 * @author Mauricio Coria
 */
public class TTableArrayPrimitives implements TTable, Acceptor {
    /*
        Data layout:
         - byte[0] = age AND TranspositionBound
         - byte[1] = draft (with sign)
         - byte[2-3] = move
         - byte[4-8] = value
     */

    static final long EXACT_BOUND_VALUE = 0b00000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long LOWER_BOUND_VALUE = 0b00000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long UPPER_BOUND_VALUE = 0b00000011_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;

    static final long AGE_MASK = 0b11111100_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long BOUND_MASK = 0b00000011_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long DRAFT_MASK = 0b00000000_11111111_00000000_00000000_00000000_00000000_00000000_00000000L;
    static final long MOVE_MASK = 0b00000000_00000000_11111111_11111111_00000000_00000000_00000000_00000000L;
    static final long VALUE_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;

    static final int MAX_AGE = 0x3F;

    int arraySize;
    long[] hashArray;
    long[] dataArray;
    int currentAge;

    private final int staleAge;

    public TTableArrayPrimitives(int staleAge, int hashSizeKB) {
        this.staleAge = staleAge;
        this.setupHashTable(hashSizeKB);
    }

    public void setupHashTable(int hashSizeKB) {
        // Suponiendo que el hashSizeKB es en KB, y se establece en DEFAULT_HASH_SIZE_KB, convertirlo a bytes
        this.arraySize = (hashSizeKB / 16) * 1024;   // 2 tablas, 8 bytes por elemento
        if (this.arraySize < 1) {
            throw new IllegalArgumentException("Hash size must be greater than 0");
        }
        this.hashArray = new long[arraySize]; // 8 bytes * 2097152 elementos = 16MB table
        this.dataArray = new long[arraySize]; // 8 bytes * 2097152 elementos = 16MB table
        this.currentAge = staleAge;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        int idx = (int) Math.abs(hash % arraySize);

        long data = dataArray[idx];

        // Extract fields from the data
        int age = (int) ((data & AGE_MASK) >>> 58);

        if (currentAge < age || currentAge - age > staleAge) {
            return false;
        }

        long bound = data & BOUND_MASK;
        byte draftByte = (byte) ((data & DRAFT_MASK) >>> 48);
        short move = (short) ((data & MOVE_MASK) >>> 32);
        int value = (int) (data & VALUE_MASK);

        // Copy stored entry fields to the output entry
        entry.hash = hashArray[idx];
        entry.draft = draftByte;
        entry.move = move;
        entry.value = value;
        entry.bound = bound == EXACT_BOUND_VALUE ? EXACT : bound == LOWER_BOUND_VALUE ? LOWER_BOUND : UPPER_BOUND;

        return true;
    }

    @Override
    public void save(TranspositionEntry entry) {
        int idx = (int) Math.abs(entry.hash % arraySize);

        hashArray[idx] = entry.hash;

        long bound = entry.bound == EXACT ? EXACT_BOUND_VALUE : entry.bound == LOWER_BOUND ? LOWER_BOUND_VALUE : UPPER_BOUND_VALUE;
        byte draftByte = entry.draft;
        short move = entry.move;
        int value = entry.value;

        dataArray[idx] = ((currentAge & 0x3FL) << 58) | bound | ((draftByte & 0xFFL) << 48) | ((move & 0xFFFFL) << 32) | (value & 0xFFFFFFFFL);
    }


    /**
     * Increments the age counter of the transposition table.
     *
     * <p>This method is used to implement an aging mechanism that helps manage entry
     * replacement in the transposition table. By tracking the age of entries, the table
     * can prioritize keeping more recent entries over older ones during replacement decisions.
     * This is typically called at the beginning of each new search iteration or game move.</p>
     *
     * <p>The age information is used in conjunction with other factors (such as search depth)
     * to determine which entries should be replaced when the table becomes full.</p>
     */
    public void increaseAge() {
        if (currentAge < MAX_AGE) {
            currentAge++;
        } else {
            clear();
            currentAge++;
        }
    }


    /**
     * Removes all entries from the transposition table, resetting it to an empty state.
     * This is typically called at the start of a new game or search session.
     */
    public void clear() {
        for (int i = 0; i < arraySize; i++) {
            hashArray[i] = 0;
            dataArray[i] = 0;
        }
        currentAge = staleAge;
    }

    public int getFillPercentage() {
        int filled = 0;
        for (int i = 0; i < arraySize; i++) {
            long data = dataArray[i];
            int age = (int) ((data & AGE_MASK) >>> 58);
            if (!(currentAge < age || currentAge - age > staleAge)) {
                filled++;
            }
        }
        return (filled * 100 / arraySize);
    }
}
