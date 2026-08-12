package net.chesstango.search.smart.sorters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationComparator;

import java.util.Comparator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, Acceptor {

    @Getter(AccessLevel.PACKAGE)
    private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

    @Setter
    private List<RootMoveEvaluation> rootMoveEvaluationList;

    RootMoveSorter(RootMoveEvaluationComparator rootMoveEvaluationComparator) {
        this.rootMoveEvaluationComparator = rootMoveEvaluationComparator.reversed();
    }

    public RootMoveSorter() {
        this(new RootMoveEvaluationComparator());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        return rootMoveEvaluationList
                .stream()
                .sorted(rootMoveEvaluationComparator)
                .map(RootMoveEvaluation::move)
                .toList();
    }

}
