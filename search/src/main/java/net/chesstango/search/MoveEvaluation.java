package net.chesstango.search;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

/**
 * @author Mauricio Coria
 */
public record MoveEvaluation(Move move,
                             int evaluation,
                             MoveEvaluationType moveEvaluationType)

        implements Comparable<MoveEvaluation> {

    @Override
    public int compareTo(MoveEvaluation other) {
        int result = Integer.compare(evaluation, other.evaluation);

        if (result == 0) {
            if (MoveEvaluationType.LOWER_BOUND.equals(moveEvaluationType) && !MoveEvaluationType.LOWER_BOUND.equals(other.moveEvaluationType)) {
                result = 1;
            } else if (MoveEvaluationType.EXACT.equals(moveEvaluationType)) {
                if (MoveEvaluationType.UPPER_BOUND.equals(other.moveEvaluationType)) {
                    result = 1;
                } else if (MoveEvaluationType.LOWER_BOUND.equals(other.moveEvaluationType)) {
                    result = -1;
                }
            } else if (MoveEvaluationType.UPPER_BOUND.equals(moveEvaluationType) && !MoveEvaluationType.UPPER_BOUND.equals(other.moveEvaluationType)) {
                result = -1;
            }
        }


        if (result == 0) {
            result = DefaultMoveComparator.compareImp(move, other.move);
        }

        return result;
    }
}
