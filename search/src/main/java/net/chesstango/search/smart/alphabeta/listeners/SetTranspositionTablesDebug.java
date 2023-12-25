package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TTableDebug;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTablesDebug extends SetTranspositionTables {

    @Override
    protected TTable createTTable() {
        return new TTableDebug(super.createTTable());
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
