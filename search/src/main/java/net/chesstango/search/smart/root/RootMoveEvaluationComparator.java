package net.chesstango.search.smart.root;

import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;

/**
 * Comparator for sorting root move evaluations.
 * Sorting priority:
 * 1. Bound type (EXACT > LOWER_BOUND > UPPER_BOUND)
 * 2. Evaluation value (higher values first)
 * 3. Move quality using DefaultMoveComparator
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationComparator implements Comparator<RootMoveEvaluation> {
    private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

    /**
     * Constructs a RootMoveEvaluationComparator with a multi-level comparison chain.
     * The comparator prioritizes bound type, then evaluation value (descending),
     * and finally move quality using the default move comparator.
     */
    public RootMoveEvaluationComparator() {
        DefaultMoveComparator defaultMoveComparator = new DefaultMoveComparator();
        this.rootMoveEvaluationComparator = Comparator
                .comparing(RootMoveEvaluation::evaluation)
                .thenComparing(RootMoveEvaluation::bound)
                .thenComparing((o1, o2) -> defaultMoveComparator.compare(o1.move(), o2.move()));

    }

    /**
     * Compares two root move evaluations using the configured comparison chain.
     *
     * @param o1 the first move evaluation to compare
     * @param o2 the second move evaluation to compare
     * @return a negative integer, zero, or a positive integer as the first argument
     * is less than, equal to, or greater than the second
     */
    @Override
    public int compare(RootMoveEvaluation o1, RootMoveEvaluation o2) {
        return rootMoveEvaluationComparator.compare(o1, o2);
    }
}
