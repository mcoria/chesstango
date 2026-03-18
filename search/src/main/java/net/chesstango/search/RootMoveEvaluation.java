package net.chesstango.search;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record RootMoveEvaluation(Move move,
                                 int evaluation,
                                 Bound bound)

        implements Comparable<RootMoveEvaluation>, Serializable {

    @Override
    public int compareTo(RootMoveEvaluation other) {
        int result = Integer.compare(evaluation, other.evaluation);

        if (result == 0) {
            if (Bound.LOWER_BOUND.equals(bound) && !Bound.LOWER_BOUND.equals(other.bound)) {
                result = 1;
            } else if (Bound.EXACT.equals(bound)) {
                if (Bound.UPPER_BOUND.equals(other.bound)) {
                    result = 1;
                } else if (Bound.LOWER_BOUND.equals(other.bound)) {
                    result = -1;
                }
            } else if (Bound.UPPER_BOUND.equals(bound) && !Bound.UPPER_BOUND.equals(other.bound)) {
                result = -1;
            }
        }


        if (result == 0) {
            result = DefaultMoveComparator.compareImp(move, other.move);
        }

        return result;
    }
}
