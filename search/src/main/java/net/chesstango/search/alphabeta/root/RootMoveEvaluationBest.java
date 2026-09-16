package net.chesstango.search.alphabeta.root;

import lombok.Getter;
import net.chesstango.search.*;

/**
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationBest implements Acceptor, SearchByDepthListener {

    @Getter
    private RootMoveEvaluation bestRootMoveEvaluation;

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
        bestRootMoveEvaluation = null;
    }


    /**
     * Saves a root move evaluation to the collection.
     *
     * @param moveEvaluation the move evaluation to save
     */
    public void save(RootMoveEvaluation moveEvaluation) {
        if (moveEvaluation.bound() == Bound.EXACT || moveEvaluation.bound() == Bound.LOWER_BOUND) {
            if (bestRootMoveEvaluation == null) {
                bestRootMoveEvaluation = moveEvaluation;
            } else if (moveEvaluation.evaluation() >= bestRootMoveEvaluation.evaluation()) {
                bestRootMoveEvaluation = moveEvaluation;
            } else {
                throw new RuntimeException("Root move evaluation value is not the same as the value returned by the search algorithm");
            }
        }
    }
}
