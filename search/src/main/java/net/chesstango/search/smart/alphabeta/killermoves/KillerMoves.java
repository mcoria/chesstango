package net.chesstango.search.smart.alphabeta.killermoves;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface KillerMoves {
    boolean trackKillerMove(Move move, int currentPly);

    boolean isKiller(Move move, int currentPly);

    void reset();
}
