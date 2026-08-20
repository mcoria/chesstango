package net.chesstango.search.sorters.visitors;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.sorters.NodeMoveSorter;

/**
 * @author Mauricio Coria
 */
public class LinkMoveToHashMap implements Visitor {
    private final MoveToHashMap moveToZobrist;

    public LinkMoveToHashMap(MoveToHashMap moveToZobrist) {
        this.moveToZobrist = moveToZobrist;
    }


    @Override
    public void visit(NodeMoveSorter nodeMoveSorter) {
        nodeMoveSorter.setMoveToZobrist(moveToZobrist);
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setMoveToZobrist(moveToZobrist);
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionTailMoveComparator) {
        transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);
    }

}
