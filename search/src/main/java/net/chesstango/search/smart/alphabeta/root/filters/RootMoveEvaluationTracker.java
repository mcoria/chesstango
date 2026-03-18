package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Bound;
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
    private RootMoveEvaluationCollection moveEvaluations;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize);
    }


    final long process(int currentPly, final int alpha, final int beta, AlphaBetaFunction fn) {
        Move currentMove = game.getHistory().peekLastRecord().playedMove();

        Optional<RootMoveEvaluation> moveEvaluation = moveEvaluations.get(currentMove);

        if (moveEvaluation.isPresent()) {
            return AlphaBetaHelper.encode(moveEvaluation.get().move(), moveEvaluation.get().evaluation());
        }

        long bestMoveAndValue = fn.search(currentPly, alpha, beta);

        trackMoveEvaluation(currentMove, bestMoveAndValue, alpha, beta);

        return bestMoveAndValue;
    }


    final void trackMoveEvaluation(Move currentMove, long bestMoveAndValue, int alpha, int beta) {
        int currentValue = AlphaBetaHelper.decodeValue(bestMoveAndValue);

        Bound moveEvaluationType = null;

        if (currentValue <= alpha) {
            moveEvaluationType = Bound.UPPER_BOUND;
        } else if (beta <= currentValue) {
            moveEvaluationType = Bound.LOWER_BOUND;
        } else {
            moveEvaluationType = Bound.EXACT;
        }

        moveEvaluations.add(new RootMoveEvaluation(currentMove, currentValue, moveEvaluationType));
    }
}
