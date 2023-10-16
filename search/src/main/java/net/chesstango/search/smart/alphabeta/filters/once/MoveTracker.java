package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Comparator;
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

    @Setter
    private StopProcessingCatch stopProcessingCatch;

    @Getter
    private List<MoveEvaluation> currentMoveEvaluations;

    @Override
    public void beforeSearch(Game game) {
        currentMoveEvaluations = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        currentMoveEvaluations = new LinkedList<>();
    }

    public void beforeSearchByWindows(int alphaBound, int betaBound) {
        //System.out.printf("Alpha=%d Beta=%d\n", alphaBound, betaBound);
    }

    public void afterSearchByWindows(boolean searchByWindowsFinished) {
        //currentMoveEvaluations.stream().sorted(Comparator.comparingInt(MoveEvaluation::evaluation)).forEach(System.out::println);
        //System.out.printf("------------\n");

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
