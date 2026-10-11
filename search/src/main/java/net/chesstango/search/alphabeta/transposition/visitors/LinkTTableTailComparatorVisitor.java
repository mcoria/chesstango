package net.chesstango.search.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;

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

    @Override
    public void visit(TranspositionTailMoveGroup transpositionTailMoveGroup) {
        transpositionTailMoveGroup.setTTable(tTable);
    }

}
