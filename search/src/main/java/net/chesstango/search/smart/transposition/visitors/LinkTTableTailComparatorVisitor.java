package net.chesstango.search.smart.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.transposition.comparators.TranspositionTailMoveComparator;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableTailComparatorVisitor implements Visitor {
    private final TTable tTable;

    public LinkTTableTailComparatorVisitor(TTable tTable) {
        this.tTable = tTable;
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setTTable(tTable);
    }

}
