package net.chesstango.search.smart.alphabeta.core;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class MoveEvaluations implements SearchByDepthListener {
    @Getter
    private List<MoveEvaluation> moveEvaluations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        this.moveEvaluations = new LinkedList<>();
    }

    public void add(MoveEvaluation moveEvaluation) {
        this.moveEvaluations.add(moveEvaluation);
    }

    public Optional<MoveEvaluation> getBestMoveEvaluation(boolean maximize) {
        Stream<MoveEvaluation> moveEvaluationStream = moveEvaluations
                .stream()
                .filter(moveEvaluation -> MoveEvaluationType.EXACT.equals(moveEvaluation.moveEvaluationType()));

        return maximize ? moveEvaluationStream.max(Comparator.comparing(MoveEvaluation::evaluation)) : moveEvaluationStream.min(Comparator.comparing(MoveEvaluation::evaluation));
    }

    public Optional<MoveEvaluation> get(Move currentMove) {
        for (MoveEvaluation evaluatedMove : moveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return Optional.of(evaluatedMove);

            }
        }
        return Optional.empty();
    }

    public void clean(int alphaBound, int betaBound) {
        moveEvaluations.removeIf(moveEvaluation -> MoveEvaluationType.UPPER_BOUND.equals(moveEvaluation.moveEvaluationType()) && alphaBound <= moveEvaluation.evaluation());
        moveEvaluations.removeIf(moveEvaluation -> MoveEvaluationType.LOWER_BOUND.equals(moveEvaluation.moveEvaluationType()) && moveEvaluation.evaluation() <= betaBound);
    }
}
