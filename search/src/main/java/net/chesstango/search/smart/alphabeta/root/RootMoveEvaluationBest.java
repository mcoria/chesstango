package net.chesstango.search.smart.alphabeta.root;

import lombok.Getter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;

import java.util.Comparator;

/**
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationBest implements Acceptor, SearchByDepthListener {

    private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

    @Getter
    private RootMoveEvaluation bestRootMoveEvaluation;

    public RootMoveEvaluationBest() {
        rootMoveEvaluationComparator = new RootMoveEvaluationComparator();
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
        bestRootMoveEvaluation = null;
    }


    /**
     * Saves a root move evaluation to the collection.
     *
     * @param moveEvaluation the move evaluation to save
     */
    public void save(RootMoveEvaluation moveEvaluation) {
        if (moveEvaluation.bound() == Bound.EXACT) {
            if (bestRootMoveEvaluation == null || moveEvaluation.evaluation() > bestRootMoveEvaluation.evaluation()) {
                bestRootMoveEvaluation = moveEvaluation;
            }
        }
    }

}
