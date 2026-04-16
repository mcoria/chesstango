package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTransposition;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTDump;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTLoad;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableImpVisitor implements Visitor {
    private final TTable tTable;

    public LinkTTableImpVisitor(TTable tTable) {
        this.tTable = tTable;
    }


    @Override
    public void visit(TTDump ttDump) {
        ttDump.setTTable(tTable);
    }

    @Override
    public void visit(TTLoad ttLoad) {
        ttLoad.setTTable(tTable);
    }

}
