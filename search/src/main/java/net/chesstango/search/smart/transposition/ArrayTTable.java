package net.chesstango.search.smart.transposition;

import java.lang.reflect.Array;

/**
 * @author Mauricio Coria
 */
public class ArrayTTable<T extends TranspositionEntry> implements TTable<T> {

    private final static int TABLE_SIZE = 1024;

    private final T[] table;

    public ArrayTTable(Class<T> theClass) {
        table = (T[]) Array.newInstance(theClass, TABLE_SIZE);
    }

    @Override
    public T read(long hash) {

        int idx = Math.abs((int) (hash % TABLE_SIZE));

        T entry = table[idx];

        if (entry == null) {
            return null;
        } else if (entry.getHash() != hash) {
            return null;
        }

        return entry;
    }

    @Override
    public void write(long hash, T entry) {
        int idx = Math.abs((int) (entry.getHash() % TABLE_SIZE));

        table[idx] = entry;
    }
}
