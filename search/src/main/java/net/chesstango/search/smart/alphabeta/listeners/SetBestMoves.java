package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetBestMoves implements SearchByCycleListener {
    @Override
    public void beforeSearch(Game game) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        List<MoveEvaluation> moveEvaluations = result.getMoveEvaluations();

        int bestValue = result.getEvaluation();

        List<Move> bestMoves = moveEvaluations.stream().filter(moveEvaluation -> moveEvaluation.evaluation() == bestValue).map(MoveEvaluation::move).toList();

        result.setBestMoves(bestMoves);
    }

}
