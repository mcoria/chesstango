package net.chesstango.search.smart.alphabeta.core.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.alphabeta.core.MoveEvaluations;

import java.util.Optional;

/**
 * Funciona como una cache de resultados y es un complemento de aspiration windows
 *
 * @author Mauricio Coria
 */
public class MoveEvaluationTracker implements AlphaBetaFilter, SearchByWindowsListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Getter
    @Setter
    private MoveEvaluations moveEvaluations;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        if (searchByWindowsCycle > 0) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
             * Dejo resultado exactos dado que no es necesario volver a explorarlos.
             * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
             */
            moveEvaluations.clean(alphaBound, betaBound);
        }
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

        Optional<MoveEvaluation> moveEvaluation = moveEvaluations.get(currentMove);

        if (moveEvaluation.isPresent()) {
            return AlphaBetaHelper.encode(moveEvaluation.get().move(), moveEvaluation.get().evaluation());
        }

        long bestMoveAndValue = fn.search(currentPly, alpha, beta);

        trackMoveEvaluation(currentMove, bestMoveAndValue, alpha, beta);

        return bestMoveAndValue;
    }


    final void trackMoveEvaluation(Move currentMove, long bestMoveAndValue, int alpha, int beta) {
        int currentValue = AlphaBetaHelper.decodeValue(bestMoveAndValue);

        MoveEvaluationType moveEvaluationType = null;

        if (currentValue <= alpha) {
            moveEvaluationType = MoveEvaluationType.UPPER_BOUND;
        } else if (beta <= currentValue) {
            moveEvaluationType = MoveEvaluationType.LOWER_BOUND;
        } else {
            moveEvaluationType = MoveEvaluationType.EXACT;
        }

        moveEvaluations.add(new MoveEvaluation(currentMove, currentValue, moveEvaluationType));
    }
}
