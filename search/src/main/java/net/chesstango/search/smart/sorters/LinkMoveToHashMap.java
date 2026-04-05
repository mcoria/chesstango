package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;

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
