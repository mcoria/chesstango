package net.chesstango.search.smart.alphabeta.killermoves.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMoves;

/**
 * @author Mauricio Coria
 */
public class KillerMoveTracker implements AlphaBetaFilter, Acceptor {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private Game game;

    @Setter
    private KillerMoves killerMoves;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int maximize(int currentPly, int alpha, int beta) {
        int currentValue = next.maximize(currentPly, alpha, beta);

        if (currentValue < alpha) {
            Move previousMove = game.getHistory().peekLastRecord().playedMove();
            killerMoves.trackKillerMove(previousMove, currentPly);
        }

        return currentValue;
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        int currentValue = next.minimize(currentPly, alpha, beta);
        if (beta < currentValue) {
            Move previousMove = game.getHistory().peekLastRecord().playedMove();
            killerMoves.trackKillerMove(previousMove, currentPly);
        }
        return currentValue;
    }
}
