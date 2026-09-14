package net.chesstango.search.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableHeadComparatorVisitor implements Visitor {
    private final TTable tTable;

    public LinkTTableHeadComparatorVisitor(TTable tTable) {
        this.tTable = tTable;
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setTTable(tTable);
    }

}
