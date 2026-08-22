package net.chesstango.search.smart.root;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCollection implements Acceptor, SearchByCycleListener {

    @Setter
    private Game game;

    @Setter
    private List<RootMoveEvaluation> rootMoveEvaluationList;

    /**
     * Accepts a visitor for the visitor pattern implementation.
     *
     * @param visitor the visitor to accept
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        rootMoveEvaluationList.clear();
        /**
         * Fake values for all possible moves.
         */
        for (Move move : game.getPossibleMoves()) {
            rootMoveEvaluationList.add(new RootMoveEvaluation(move, Evaluator.INFINITE_NEGATIVE, Bound.UPPER_BOUND, null));
        }
    }

    /**
     * Saves a root move evaluation to the collection.
     *
     * @param moveEvaluation the move evaluation to save
     */
    public void save(RootMoveEvaluation moveEvaluation) {
        if (rootMoveEvaluationList.removeIf(rootMoveEvaluation -> rootMoveEvaluation.move().equals(moveEvaluation.move()))) {
            rootMoveEvaluationList.add(moveEvaluation);
        } else {
            throw new RuntimeException("Move should exist in rootMoveEvaluationList.");
        }
    }

    public List<RootMoveEvaluation> getRootMoveEvaluationListCopy() {
        return List.copyOf(rootMoveEvaluationList);
    }
}
