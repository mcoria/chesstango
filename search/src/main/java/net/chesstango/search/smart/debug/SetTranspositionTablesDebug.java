package net.chesstango.search.smart.debug;

import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.debug.TTableDebug;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTablesDebug extends SetTranspositionTables {

    @Override
    protected TTable createQMinTTable() {
        return new TTableDebug("qMinMap", defaultCreateTTable());
    }

    @Override
    protected TTable createQMaxTTable() {
        return new TTableDebug("qMaxMap", defaultCreateTTable());
    }

    @Override
    protected TTable createMinTTable() {
        return new TTableDebug("minMap", defaultCreateTTable());
    }

    @Override
    protected TTable createMaxTTable() {
        return new TTableDebug("maxMap", defaultCreateTTable());
    }

    public TTableDebug getMaxMap() {
        return (TTableDebug) maxMap;
    }

    public TTableDebug getMinMap() {
        return (TTableDebug) minMap;
    }

    public TTableDebug getQMaxMap() {
        return (TTableDebug) qMaxMap;
    }

    public TTableDebug getQMinMap() {
        return (TTableDebug) qMinMap;
    }
}
