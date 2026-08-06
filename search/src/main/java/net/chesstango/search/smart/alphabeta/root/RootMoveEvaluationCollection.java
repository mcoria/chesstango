package net.chesstango.search.smart.alphabeta.root;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the collection of root move evaluations during an alpha-beta search.
 * This class tracks and maintains evaluations of all moves available at the root position,
 * handling aspiration windows, sorting, and determining the best move based on evaluation results.
 * It implements multiple listener interfaces to respond to different phases of the search process.
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCollection implements Acceptor, SearchByDepthListener, SearchByWindowsListener {

    private final List<RootMoveEvaluation> rootMoveEvaluations;

    private final RootMoveEvaluationComparator rootMoveEvaluationComparator;

    private boolean searchingByWindows;

    @Getter
    private RootMoveEvaluation bestRootMoveEvaluation;

    /**
     * Constructs a new RootMoveEvaluationCollection.
     * Initializes the internal list of move evaluations and the comparator used for sorting.
     */
    public RootMoveEvaluationCollection() {
        rootMoveEvaluations = new LinkedList<>();
        rootMoveEvaluationComparator = new RootMoveEvaluationCollection.RootMoveEvaluationComparator();
    }

    /**
     * Accepts a visitor for the visitor pattern implementation.
     *
     * @param visitor the visitor to accept
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    /**
     * Called before searching at a new depth level.
     * Clears all move evaluations to prepare for the new depth iteration.
     */
    @Override
    public void beforeSearchByDepth() {
        rootMoveEvaluations.clear();
        bestRootMoveEvaluation = null;
        searchingByWindows = false;
    }


    @Override
    public void afterSearchByDepth(boolean searchStopped) {
        if (!searchingByWindows && !rootMoveEvaluations.isEmpty()) {
            rootMoveEvaluations.sort(rootMoveEvaluationComparator);
            bestRootMoveEvaluation = rootMoveEvaluations.getFirst();
        }
    }


    /**
     * Called before searching within a new aspiration window.
     * For re-searches (cycle > 0), removes move evaluations that don't need to be re-explored:
     * - Keeps exact results (no need to re-explore)
     * - Keeps non-exact results that still fall within the new window bounds
     * - Removes upper bounds that failed low relative to the new alpha
     * - Removes lower bounds that failed high relative to the new beta
     *
     * @param alpha                the lower bound of the aspiration window
     * @param beta                 the upper bound of the aspiration window
     * @param searchByWindowsCycle the current aspiration window cycle number (0 for initial search)
     */
    @Override
    public void beforeSearchByWindows(int alpha, int beta, int searchByWindowsCycle) {
        searchingByWindows = true;
        /**
         * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
         * Dejo resultado exactos dado que no es necesario volver a explorarlos.
         * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
         */
        rootMoveEvaluations.removeIf(moveEvaluation -> Bound.UPPER_BOUND.equals(moveEvaluation.bound()) && alpha <= moveEvaluation.evaluation());
        rootMoveEvaluations.removeIf(moveEvaluation -> Bound.LOWER_BOUND.equals(moveEvaluation.bound()) && moveEvaluation.evaluation() <= beta);
    }

    @Override
    public void afterSearchByWindows(boolean searchStopped) {
        if (!rootMoveEvaluations.isEmpty()) {
            rootMoveEvaluations.sort(rootMoveEvaluationComparator);
            /**
             * En caso de detenerse la busqueda, esta condicion garantiza el mejor resultado:
             * - Del primer ciclo INCOMPLETO
             * - Del ciclo anterior COMPLETO
             */
            if (bestRootMoveEvaluation == null || !searchStopped) {
                bestRootMoveEvaluation = rootMoveEvaluations.getFirst();
            }
        }
    }


    /**
     * Saves a root move evaluation to the collection.
     *
     * @param moveEvaluation the move evaluation to save
     */
    public void save(RootMoveEvaluation moveEvaluation) {
        rootMoveEvaluations.add(moveEvaluation);
    }


    /**
     * Retrieves the evaluation for a specific move if it exists in the collection.
     *
     * @param currentMove the move to look up
     * @return an Optional containing the RootMoveEvaluation if found, or empty if not found
     */
    public Optional<RootMoveEvaluation> get(Move currentMove) {
        for (RootMoveEvaluation evaluatedMove : rootMoveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return Optional.of(evaluatedMove);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an immutable copy of all root move evaluations in the collection.
     *
     * @return an immutable list containing all move evaluations
     */
    public List<RootMoveEvaluation> getRootMoveEvaluations() {
        return List.copyOf(rootMoveEvaluations);
    }

    /**
     * Comparator for sorting root move evaluations.
     * Sorting priority:
     * 1. Bound type (EXACT > LOWER_BOUND > UPPER_BOUND)
     * 2. Evaluation value (higher values first)
     * 3. Move quality using DefaultMoveComparator
     *
     * @author Mauricio Coria
     */
    public static class RootMoveEvaluationComparator implements Comparator<RootMoveEvaluation> {
        private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

        /**
         * Constructs a RootMoveEvaluationComparator with a multi-level comparison chain.
         * The comparator prioritizes bound type, then evaluation value (descending),
         * and finally move quality using the default move comparator.
         */
        public RootMoveEvaluationComparator() {
            DefaultMoveComparator defaultMoveComparator = new DefaultMoveComparator();
            this.rootMoveEvaluationComparator = Comparator
                    .comparing(RootMoveEvaluation::evaluation, Comparator.reverseOrder()) // De mayor a menor
                    .thenComparing(RootMoveEvaluation::bound, Comparator.reverseOrder())
                    .thenComparing((o1, o2) -> defaultMoveComparator.reversed().compare(o1.move(), o2.move()));

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
}
