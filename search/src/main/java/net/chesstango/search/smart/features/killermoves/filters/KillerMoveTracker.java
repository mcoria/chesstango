package net.chesstango.search.smart.features.killermoves.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class KillerMoveTracker implements AlphaBetaFilter, SearchByCycleListener {


    @Setter
    @Getter
    private AlphaBetaFilter next;

    private Game game;
    private KillerMoves killerMoves;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.killerMoves = context.getKillerMoves();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);

        if (currentValue < alpha) {
            Move previousMove = game.getHistory().peekLastRecord().playedMove();
            killerMoves.trackKillerMove(previousMove, currentPly);
        }

        return moveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);
        if (beta < currentValue) {
            Move previousMove = game.getHistory().peekLastRecord().playedMove();
            killerMoves.trackKillerMove(previousMove, currentPly);
        }
        return moveAndValue;
    }
}
