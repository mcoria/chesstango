package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public class ArrayTTable implements TTable {

    private static final int ARRAY_SIZE = 1024 * 512;
    private final TranspositionEntry[] transpositionArray;
    private final int[] sessionIds;
    private int currentSessionId;

    public ArrayTTable() {
        transpositionArray = new TranspositionEntry[ARRAY_SIZE];
        sessionIds = new int[ARRAY_SIZE];
        currentSessionId = 0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            transpositionArray[i] = new TranspositionEntry();
        }
    }

    @Override
    public TranspositionEntry get(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        TranspositionEntry entry = transpositionArray[idx];
        int entrySessionId = sessionIds[idx];

        return hash == entry.hash && entrySessionId == currentSessionId ? entry : null;
    }

    @Override
    public TranspositionEntry allocate(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);
        sessionIds[idx] = currentSessionId;
        return transpositionArray[idx];
    }

    @Override
    public void clear() {
        currentSessionId++;
    }
}
