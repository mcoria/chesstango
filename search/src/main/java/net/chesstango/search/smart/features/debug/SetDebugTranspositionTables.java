package net.chesstango.search.smart.features.debug;

import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.features.transposition.TTable;

/**
 * @author Mauricio Coria
 */
public class SetDebugTranspositionTables extends SetTranspositionTables {

    @Override
    protected TTable createQMinTTable() {
        return new TrapTranspositionAccess(DebugOperationTT.TableType.MIN_MAP_Q, defaultCreateTTable());
    }

    @Override
    protected TTable createQMaxTTable() {
        return new TrapTranspositionAccess(DebugOperationTT.TableType.MAX_MAP_Q, defaultCreateTTable());
    }

    @Override
    protected TTable createMinTTable() {
        return new TrapTranspositionAccess(DebugOperationTT.TableType.MIN_MAP, defaultCreateTTable());
    }

    @Override
    protected TTable createMaxTTable() {
        return new TrapTranspositionAccess(DebugOperationTT.TableType.MAX_MAP, defaultCreateTTable());
    }

    public TrapTranspositionAccess getMaxMap() {
        return (TrapTranspositionAccess) maxMap;
    }

    public TrapTranspositionAccess getMinMap() {
        return (TrapTranspositionAccess) minMap;
    }

    public TrapTranspositionAccess getQMaxMap() {
        return (TrapTranspositionAccess) qMaxMap;
    }

    public TrapTranspositionAccess getQMinMap() {
        return (TrapTranspositionAccess) qMinMap;
    }
}
