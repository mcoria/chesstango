package net.chesstango.search.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.filters.*;

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
    public void visit(TranspositionTableInterior transpositionTableInterior) {
        transpositionTableInterior.setTTable(tTable);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setTTable(tTable);
    }
}
