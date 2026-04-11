package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

import java.util.Optional;

/**
 * Actualiza RootMoveEvaluationCollection a medida que se obtienen resultados de los movimientos de root node
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationTracker implements AlphaBetaFilter {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Getter
    @Setter
    private RootMoveEvaluationCollection rootMoveEvaluationCollection;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize);
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize);
    }


    final int process(int currentPly, final int alpha, final int beta, AlphaBetaFunction fn) {
        Move currentMove = game.getHistory().peekLastRecord().playedMove();

        Optional<RootMoveEvaluation> moveEvaluation = rootMoveEvaluationCollection.get(currentMove);

        if (moveEvaluation.isPresent()) {
            RootMoveEvaluation rootMoveEvaluation = moveEvaluation.get();
            return rootMoveEvaluation.evaluation();
        }

        int currentValue = fn.search(currentPly, alpha, beta);

        rootMoveEvaluationCollection.save(createRootMoveEvaluation(currentMove, currentValue, alpha, beta));

        return currentValue;
    }


    final RootMoveEvaluation createRootMoveEvaluation(Move currentMove, int currentValue, int alpha, int beta) {
        Bound moveEvaluationType = null;

        if (currentValue <= alpha) {
            moveEvaluationType = Bound.UPPER_BOUND;
        } else if (beta <= currentValue) {
            moveEvaluationType = Bound.LOWER_BOUND;
        } else {
            moveEvaluationType = Bound.EXACT;
        }

        return new RootMoveEvaluation(currentMove, currentValue, moveEvaluationType);
    }
}
