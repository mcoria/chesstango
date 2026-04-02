package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, SearchByCycleListener {

    @Getter
    @Setter
    private MoveSorter next;

    @Setter
    private Game game;

    @Setter
    private List<RootMoveEvaluation> lastRootMoveEvaluations;

    private int numberOfMove;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.numberOfMove = game.getPossibleMoves().size();
        this.lastRootMoveEvaluations = null;

    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        if (lastRootMoveEvaluations == null) {
            return next.getOrderedMoves(currentPly);
        } else {
            return getSortedMovesByLastMoveEvaluations();
        }
    }


    private List<Move> getSortedMovesByLastMoveEvaluations() {
        if (lastRootMoveEvaluations.size() != numberOfMove) {
            throw new RuntimeException("Not all move were explorer during last iteration");
        }

        return lastRootMoveEvaluations.stream().map(RootMoveEvaluation::move).toList();
    }

}
