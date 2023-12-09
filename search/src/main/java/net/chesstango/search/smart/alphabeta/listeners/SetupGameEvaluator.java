package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class SetupGameEvaluator implements SearchByCycleListener {

    @Setter
    private GameEvaluator gameEvaluator;

    @Override
    public void beforeSearch(Game game) {
        gameEvaluator.setGame(game);
    }


    @Override
    public void afterSearch(SearchMoveResult result) {
    }

}
