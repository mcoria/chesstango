package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTransposition;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.filters.*;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableNodeVisitor implements Visitor {
    private final TTable tTable;

    public LinkTTableNodeVisitor(TTable tTable) {
        this.tTable = tTable;
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setTTable(tTable);
    }

    @Override
    public void visit(TranspositionTableTerminal transpositionTableTerminal) {
        transpositionTableTerminal.setTTable(tTable);
    }

    @Override
    public void visit(TranspositionTableLeaf transpositionTableLeaf) {
        transpositionTableLeaf.setTTable(tTable);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setTTable(tTable);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setTTable(tTable);
    }

    @Override
    public void visit(PVCalculatorTransposition pvCalculatorTransposition) {
        pvCalculatorTransposition.setTTable(tTable);
    }
}
