package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
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

            // Una vez ejecutadas la busqueda DEPTH N-1, la busqueda en DEPTH N:

            if (lastRootMoveEvaluations.size() != numberOfMove) {
                throw new RuntimeException("Not all move were explorer during last iteration");
            }

            if (Bound.EXACT != lastRootMoveEvaluations.getFirst().bound()) {
                throw new RuntimeException("First move bound is not exact after sorting");
            }

            return lastRootMoveEvaluations.stream().map(RootMoveEvaluation::move).toList();
        }
    }

}
