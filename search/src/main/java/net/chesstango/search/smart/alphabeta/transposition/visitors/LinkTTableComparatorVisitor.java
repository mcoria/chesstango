package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableComparatorVisitor implements Visitor {
    private final TTable tTable;

    public LinkTTableComparatorVisitor(TTable tTable) {
        this.tTable = tTable;
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setTTable(tTable);
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setTTable(tTable);
    }

}
