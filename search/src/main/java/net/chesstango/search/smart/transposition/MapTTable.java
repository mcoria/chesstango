package net.chesstango.search.smart.transposition;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
class MapTTable<T extends TranspositionEntry> implements TTable<T> {

    private Map<Long, T> table = new HashMap<>();

    private final Class<T> theClass;

    public MapTTable(Class<T> theClas) {
        this.theClass = theClas;
    }

    @Override
    public boolean read(long hash, T entry) {
        T theEntry = table.get(hash);

        if (theEntry == null) {
            return false;
        }

        entry.loadValues(theEntry);

        return true;
    }

    @Override
    public void write(long hash, T entry) {
        try {

            T theEntry = theClass.getDeclaredConstructor().newInstance();

            theEntry.loadValues(entry);

            table.put(hash, theEntry);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
