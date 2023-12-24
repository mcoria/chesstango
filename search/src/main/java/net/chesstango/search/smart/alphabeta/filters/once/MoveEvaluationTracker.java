package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class MoveEvaluationTracker implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private StopProcessingCatch stopProcessingCatch;

    @Getter
    private List<MoveEvaluation> currentMoveEvaluations;
    private Game game;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.currentMoveEvaluations = null;
        this.game = context.getGame();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        currentMoveEvaluations = new LinkedList<>();
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound) {
    }

    @Override
    public void afterSearchByWindows(boolean searchByWindowsFinished) {
        if (!searchByWindowsFinished) {
            if (Objects.nonNull(stopProcessingCatch)) {
                //stopProcessingCatch.setCurrentMoveEvaluations(currentMoveEvaluations);
            }

            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva
             */
            currentMoveEvaluations = new LinkedList<>();
        }
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        result.setMoveEvaluations(currentMoveEvaluations);

        int bestValue = result.getEvaluation();
        List<Move> bestMoves = currentMoveEvaluations.stream()
                .filter(moveEvaluation -> moveEvaluation.evaluation() == bestValue)
                .map(MoveEvaluation::move)
                .toList();

        result.setBestMoves(bestMoves);
    }

    @Override
    public void afterSearch() {
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);
        trackMove(bestMoveAndValue);
        return bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);
        trackMove(bestMoveAndValue);
        return bestMoveAndValue;
    }

    private void trackMove(long bestMoveAndValue) {
        Move currentMove = game.getState().getPreviousState().getSelectedMove();
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        currentMoveEvaluations.add(new MoveEvaluation(currentMove, currentValue));
    }

}
