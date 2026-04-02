package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;

public class RootMoveEvaluationComparator implements Comparator<RootMoveEvaluation> {
    private final Comparator<Move> defaultMoveComparator = new DefaultMoveComparator();

    @Override
    public int compare(RootMoveEvaluation o1, RootMoveEvaluation o2) {
        int result = o1.compareTo(o2);
        if (result == 0) {
            return defaultMoveComparator.compare(o1.move(), o2.move());
        }
        return result;
    }
}
