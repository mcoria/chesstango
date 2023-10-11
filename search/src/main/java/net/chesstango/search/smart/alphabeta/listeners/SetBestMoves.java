package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetBestMoves implements SearchLifeCycle {
    @Override
    public void beforeSearch(Game game, int maxDepth) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {

    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        List<MoveEvaluation> moveEvaluations = result.getMoveEvaluations();

        int bestValue = result.getEvaluation();

        List<Move> bestMoves = moveEvaluations.stream().filter(moveEvaluation -> moveEvaluation.evaluation() == bestValue).map(MoveEvaluation::move).toList();

        result.setBestMoves(bestMoves);
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }
}
