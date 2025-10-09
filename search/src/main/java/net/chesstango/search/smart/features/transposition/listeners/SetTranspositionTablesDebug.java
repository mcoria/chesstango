package net.chesstango.search.smart.features.transposition.listeners;

import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TTableDebug;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTablesDebug extends SetTranspositionTables {

    @Override
    protected TTable createQMinTTable() {
        return new TTableDebug(DebugOperationTT.TableType.MIN_MAP_Q, defaultCreateTTable());
    }

    @Override
    protected TTable createQMaxTTable() {
        return new TTableDebug(DebugOperationTT.TableType.MAX_MAP_Q, defaultCreateTTable());
    }

    @Override
    protected TTable createMinTTable() {
        return new TTableDebug(DebugOperationTT.TableType.MIN_MAP, defaultCreateTTable());
    }

    @Override
    protected TTable createMaxTTable() {
        return new TTableDebug(DebugOperationTT.TableType.MAX_MAP, defaultCreateTTable());
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
