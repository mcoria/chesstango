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
            result = bound.compareTo(other.bound);
        }


        /**
         * Me parece que esto no va, deja de actuar el proximo comparador cuando result == 0
         */
        if (result == 0) {
            result = DefaultMoveComparator.compareImp(move, other.move);
        }

        return result;
    }
}
