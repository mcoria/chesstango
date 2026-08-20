package net.chesstango.search.smart.root;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCache implements Acceptor, SearchByDepthListener, SearchByWindowsListener {

    private List<RootMoveEvaluation> rootMoveEvaluations;


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
        rootMoveEvaluations = new ArrayList<>();
    }


    /**
     * Called before searching within a new aspiration window.
     * Removes move evaluations that don't need to be re-explored:
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
        /**
         * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
         * Dejo resultado exactos dado que no es necesario volver a explorarlos.
         * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
         */
        rootMoveEvaluations.removeIf(moveEvaluation -> Bound.UPPER_BOUND.equals(moveEvaluation.bound()) && alpha <= moveEvaluation.evaluation());
        rootMoveEvaluations.removeIf(moveEvaluation -> Bound.LOWER_BOUND.equals(moveEvaluation.bound()) && moveEvaluation.evaluation() <= beta);
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

}
