package net.chesstango.search.smart.sorters;

import net.chesstango.board.Color;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;

/**
 * @author Mauricio Coria
 */
public class RootMoveEvaluationComparator implements Comparator<RootMoveEvaluation> {
    private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

    public RootMoveEvaluationComparator(Color color) {
        DefaultMoveComparator defaultMoveComparator = new DefaultMoveComparator();
        this.rootMoveEvaluationComparator = Color.WHITE.equals(color)
                ? Comparator
                  .comparing(RootMoveEvaluation::bound, Comparator.reverseOrder())
                  .thenComparing(RootMoveEvaluation::evaluation, Comparator.reverseOrder())         // De mayor a menor
                  .thenComparing((o1, o2) -> defaultMoveComparator.reversed().compare(o1.move(), o2.move()))
                : Comparator
                  .comparing(RootMoveEvaluation::bound)
                  .thenComparing(RootMoveEvaluation::evaluation)                                   // De menor a mayor: natural order
                  .thenComparing((o1, o2) -> defaultMoveComparator.reversed().compare(o1.move(), o2.move()));

    }

    @Override
    public int compare(RootMoveEvaluation o1, RootMoveEvaluation o2) {
        return rootMoveEvaluationComparator.compare(o1, o2);
    }
}
