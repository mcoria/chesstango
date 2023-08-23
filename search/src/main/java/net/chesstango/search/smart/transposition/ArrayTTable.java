package net.chesstango.search.smart.transposition;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mauricio Coria
 */
public class ArrayTTable<T extends TranspositionEntry> implements TTable<T> {

    private final static int TABLE_SIZE = 1024;

    private final T[] table;

    public ArrayTTable(Class<T> theClass) {
        table = (T[]) Array.newInstance(theClass, TABLE_SIZE);

        try {
            for (int i = 0; i < TABLE_SIZE; i++) {
                table[i] = theClass.getDeclaredConstructor().newInstance();
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public boolean read(long hash, T entry) {

        int idx = Math.abs((int) (hash % TABLE_SIZE));

        T refEntry = table[idx];

        if (refEntry == null) {
            return false;
        } else if (refEntry.getHash() != hash) {
            return false;
        }

        entry.loadValues(refEntry);

        return true;
    }

    @Override
    public void write(long hash, T entry) {
        int idx = Math.abs((int) (entry.getHash() % TABLE_SIZE));

        table[idx].loadValues(entry);
    }
}
