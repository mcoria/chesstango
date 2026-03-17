package net.chesstango.search;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record RootChildEvaluation(Move move,
                                  int evaluation,
                                  Bound moveEvaluationType)

        implements Comparable<RootChildEvaluation>, Serializable {

    @Override
    public int compareTo(RootChildEvaluation other) {
        int result = Integer.compare(evaluation, other.evaluation);

        if (result == 0) {
            if (Bound.LOWER_BOUND.equals(moveEvaluationType) && !Bound.LOWER_BOUND.equals(other.moveEvaluationType)) {
                result = 1;
            } else if (Bound.EXACT.equals(moveEvaluationType)) {
                if (Bound.UPPER_BOUND.equals(other.moveEvaluationType)) {
                    result = 1;
                } else if (Bound.LOWER_BOUND.equals(other.moveEvaluationType)) {
                    result = -1;
                }
            } else if (Bound.UPPER_BOUND.equals(moveEvaluationType) && !Bound.UPPER_BOUND.equals(other.moveEvaluationType)) {
                result = -1;
            }
        }


        if (result == 0) {
            result = DefaultMoveComparator.compareImp(move, other.move);
        }

        return result;
    }
}
