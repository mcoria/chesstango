package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class MoveTracker implements AlphaBetaFilter {
    @Setter
    private AlphaBetaFilter next;

    @Setter
    private AlphaBetaFirst alphaBetaFirst;

    private List<MoveEvaluation> currentMoveEvaluations;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        currentMoveEvaluations = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        currentMoveEvaluations = new LinkedList<>();
    }

    public void beforeSearchByWindows(int alphaBound, int betaBound) {
    }

    public void afterSearchByWindows(boolean searchByWindowsFinished) {
        if (!searchByWindowsFinished) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva
             */
            currentMoveEvaluations.clear();
        }
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        result.setMoveEvaluations(currentMoveEvaluations);
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {

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
        Move currentMove = alphaBetaFirst.getCurrentMove();
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        currentMoveEvaluations.add(new MoveEvaluation(currentMove, currentValue));
    }

}
