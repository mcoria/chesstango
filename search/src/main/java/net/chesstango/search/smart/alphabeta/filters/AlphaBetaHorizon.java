package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaHorizon implements AlphaBetaFilter, SearchByCycleListener {

    @Setter
    @Getter
    private AlphaBetaFilter quiescence;

    @Setter
    private GameEvaluator gameEvaluator;

    private Game game;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void afterSearch() {

    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        if (isCurrentPositionQuiet()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate());
        } else {
            return quiescence.maximize(currentPly, alpha, beta);
        }
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (isCurrentPositionQuiet()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate());
        } else {
            return quiescence.minimize(currentPly, alpha, beta);
        }
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}
