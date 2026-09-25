package net.chesstango.search.alphabeta.root;

import net.chesstango.search.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationBest implements Acceptor, SearchByDepthListener {

    private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

    private List<RootMoveEvaluation> bestRootMoves;


    public RootMoveEvaluationBest() {
        rootMoveEvaluationComparator = new RootMoveEvaluationComparator().reversed();
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
        bestRootMoves = new ArrayList<>();
    }


    /**
     * Saves a root move evaluation to the collection.
     *
     * @param moveEvaluation the move evaluation to save
     */
    public void save(RootMoveEvaluation moveEvaluation) {
        bestRootMoves.removeIf(rootMoveEvaluation -> rootMoveEvaluation.move().equals(moveEvaluation.move()));
        if (moveEvaluation.bound() == Bound.EXACT || moveEvaluation.bound() == Bound.LOWER_BOUND) {
            bestRootMoves.add(moveEvaluation);
        }
    }

    public RootMoveEvaluation getBestRootMoveEvaluation() {
        bestRootMoves.sort(rootMoveEvaluationComparator);
        return !bestRootMoves.isEmpty() ? bestRootMoves.getFirst() : null;
    }
}
