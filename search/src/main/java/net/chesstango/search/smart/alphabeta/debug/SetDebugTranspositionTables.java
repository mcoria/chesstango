package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */
public class SetDebugTranspositionTables extends SetTranspositionTables {

    @Override
    protected TTable createQMinTTable() {
        return new DebugTT(DebugNodeTT.TableType.MIN_MAP_Q, defaultCreateTTable());
    }

    @Override
    protected TTable createQMaxTTable() {
        return new DebugTT(DebugNodeTT.TableType.MAX_MAP_Q, defaultCreateTTable());
    }

    @Override
    protected TTable createMinTTable() {
        return new DebugTT(DebugNodeTT.TableType.MIN_MAP, defaultCreateTTable());
    }

    @Override
    protected TTable createMaxTTable() {
        return new DebugTT(DebugNodeTT.TableType.MAX_MAP, defaultCreateTTable());
    }

    public DebugTT getMaxMap() {
        return (DebugTT) maxMap;
    }

    public DebugTT getMinMap() {
        return (DebugTT) minMap;
    }

    public DebugTT getQMaxMap() {
        return (DebugTT) qMaxMap;
    }

    public DebugTT getQMinMap() {
        return (DebugTT) qMinMap;
    }
}
