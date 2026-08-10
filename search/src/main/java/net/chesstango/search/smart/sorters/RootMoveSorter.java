package net.chesstango.search.smart.sorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationComparator;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, Acceptor {

    private final RootMoveEvaluationComparator rootMoveEvaluationComparator = new RootMoveEvaluationComparator();

    @Setter
    private List<RootMoveEvaluation> rootMoveEvaluationList;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        rootMoveEvaluationList.sort(rootMoveEvaluationComparator.reversed());

        return rootMoveEvaluationList.stream().map(RootMoveEvaluation::move).toList();
    }

}
