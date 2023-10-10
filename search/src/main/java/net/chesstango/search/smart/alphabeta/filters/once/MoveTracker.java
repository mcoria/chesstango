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

    private List<MoveEvaluation> moveEvaluations;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        moveEvaluations = new LinkedList<>();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        result.setMoveEvaluations(moveEvaluations);

        Integer bestValue = alphaBetaFirst.getBestValue();
        if (Objects.nonNull(bestValue)) {
            List<Move> bestMoves = moveEvaluations
                    .stream()
                    .filter(moveEvaluation -> moveEvaluation.evaluation() == bestValue)
                    .map(MoveEvaluation::move)
                    .toList();

            result.setBestMoves(bestMoves);
        }

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
        moveEvaluations.add(new MoveEvaluation(currentMove, currentValue));
    }
}
