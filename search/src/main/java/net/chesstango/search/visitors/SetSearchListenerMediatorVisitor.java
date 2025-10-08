package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.features.transposition.listeners.SetTranspositionTables;

/**
 *
 * @author Mauricio Coria
 */
public class SetSearchListenerMediatorVisitor implements Visitor {
    private final SearchListenerMediator searchListenerMediator;

    public SetSearchListenerMediatorVisitor(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public void visit(SetSearchTracker setSearchTracker) {
        setSearchTracker.setSearchListenerMediator(searchListenerMediator);
    }

    @Override
    public void visit(SetKillerMoveTables setKillerMoveTables){
        setKillerMoveTables.setSearchListenerMediator(searchListenerMediator);
    }

    @Override
    public void visit(SetKillerMoveTablesDebug setKillerMoveTablesDebug) {
        setKillerMoveTablesDebug.setSearchListenerMediator(searchListenerMediator);
    }

    @Override
    public void visit(SetNodeStatistics setNodeStatistics) {
        setNodeStatistics.setSearchListenerMediator(searchListenerMediator);
    }

    @Override
    public void visit(SetTranspositionTables setTranspositionTables) {
        setTranspositionTables.setSearchListenerMediator(searchListenerMediator);
    }

    @Override
    public void visit(SetTrianglePV setTrianglePV) {
        setTrianglePV.setSearchListenerMediator(searchListenerMediator);
    }

}
