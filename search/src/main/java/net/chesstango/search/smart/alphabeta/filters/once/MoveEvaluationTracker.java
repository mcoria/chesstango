package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Funciona como una cache de resultados y es un complemento de aspiration windows
 *
 * @author Mauricio Coria
 */
public class MoveEvaluationTracker implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    private List<MoveEvaluation> currentMoveEvaluations;

    private Map<Short, Long> moveToMoveAndValueMap;

    private Game game;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.currentMoveEvaluations = null;
        this.game = context.getGame();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        currentMoveEvaluations = new LinkedList<>();
        moveToMoveAndValueMap = new HashMap<>();
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        if (searchByWindowsCycle > 0) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
             * Dejo los movimientos exactos dado que no es necesario volver a explorarlos.
             */
            currentMoveEvaluations.removeIf(moveEvaluation -> MoveEvaluationType.UPPER_BOUND.equals(moveEvaluation.moveEvaluationType()) && alphaBound <= moveEvaluation.evaluation());
            currentMoveEvaluations.removeIf(moveEvaluation -> MoveEvaluationType.LOWER_BOUND.equals(moveEvaluation.moveEvaluationType()) && moveEvaluation.evaluation() <= betaBound);
        }
    }

    @Override
    public void afterSearchByWindows(boolean searchByWindowsFinished) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        result.setMoveEvaluations(currentMoveEvaluations);

        int bestValue = result.getBestEvaluation();
        List<Move> possibleCollisions = currentMoveEvaluations.stream()
                .filter(moveEvaluation -> moveEvaluation.evaluation() == bestValue)
                .filter(moveEvaluation -> !MoveEvaluationType.EXACT.equals(moveEvaluation.moveEvaluationType()))
                .map(MoveEvaluation::move)
                .toList();

        result.setPossibleCollisions(possibleCollisions);
    }

    @Override
    public void afterSearch() {
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize);
    }

    private long process(int currentPly, final int alpha, final int beta, AlphaBetaFunction fn) {
        Move currentMove = game.getState().getPreviousState().getSelectedMove();

        for (MoveEvaluation evaluatedMove : currentMoveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return moveToMoveAndValueMap.get(evaluatedMove.move().binaryEncoding());
            }
        }

        long bestMoveAndValue = fn.search(currentPly, alpha, beta);

        trackMove(currentMove, bestMoveAndValue, alpha, beta);

        return bestMoveAndValue;
    }


    private void trackMove(Move currentMove, long bestMoveAndValue, int alpha, int beta) {
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        MoveEvaluationType moveEvaluationType = null;

        if (currentValue <= alpha) {
            moveEvaluationType = MoveEvaluationType.UPPER_BOUND;
        } else if (beta <= currentValue) {
            moveEvaluationType = MoveEvaluationType.LOWER_BOUND;
        } else {
            moveEvaluationType = MoveEvaluationType.EXACT;
        }


        currentMoveEvaluations.add(new MoveEvaluation(currentMove, currentValue, moveEvaluationType));
        moveToMoveAndValueMap.put(currentMove.binaryEncoding(), bestMoveAndValue);
    }

}
