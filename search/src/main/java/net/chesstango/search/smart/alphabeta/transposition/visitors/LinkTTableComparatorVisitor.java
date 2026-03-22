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
    private final TTable maxMap;
    private final TTable minMap;

    public LinkTTableComparatorVisitor(TTable maxMap, TTable minMap) {
        this.maxMap = maxMap;
        this.minMap = minMap;
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setMaxMap(maxMap);
        transpositionHeadMoveComparator.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setMaxMap(maxMap);
        transpositionHeadMoveComparator.setMinMap(minMap);
    }

}
