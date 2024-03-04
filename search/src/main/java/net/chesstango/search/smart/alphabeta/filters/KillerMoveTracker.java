package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class KillerMoveTracker implements AlphaBetaFilter, SearchByCycleListener {


    @Setter
    @Getter
    private AlphaBetaFilter next;

    private Game game;
    private Move[] killerMovesTableA;
    private Move[] killerMovesTableB;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.killerMovesTableA = context.getKillerMovesTableA();
        this.killerMovesTableB = context.getKillerMovesTableB();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);

        if (currentValue < alpha) {
            Move previousMove = game.getState().getPreviousState().getSelectedMove();
            trackKillerMove(currentPly, previousMove);
        }

        return moveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);
        if (beta < currentValue) {
            Move previousMove = game.getState().getPreviousState().getSelectedMove();
            trackKillerMove(currentPly, previousMove);
        }
        return moveAndValue;
    }

    private void trackKillerMove(int currentPly, Move killerMove) {
        if (!Objects.equals(killerMove, killerMovesTableA[currentPly - 2]) && !Objects.equals(killerMove, killerMovesTableB[currentPly - 2])) {
            killerMovesTableB[currentPly - 2] = killerMovesTableA[currentPly];
            killerMovesTableA[currentPly - 2] = killerMove;
        }
    }
}
