package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, Acceptor, SearchByCycleListener {
    @Getter
    @Setter
    private MoveSorter next;

    @Setter
    private Game game;

    @Setter
    private List<RootMoveEvaluation> rootMoveEvaluationList;

    private int numberOfMove;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.numberOfMove = game.getPossibleMoves().size();
        this.rootMoveEvaluationList = null;
    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        if (rootMoveEvaluationList == null) {
            return next.getOrderedMoves(currentPly);
        } else {

            // Una vez ejecutadas la busqueda DEPTH N-1, la busqueda en DEPTH N:

            if (rootMoveEvaluationList.size() != numberOfMove) {
                throw new RuntimeException("Not all move were explorer during last iteration");
            }

            if (Bound.EXACT != rootMoveEvaluationList.getFirst().bound()) {
                throw new RuntimeException("First move bound is not exact after sorting");
            }

            return rootMoveEvaluationList.stream().map(RootMoveEvaluation::move).toList();
        }
    }

}
