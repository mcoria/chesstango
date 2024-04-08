package net.chesstango.search.smart.killermoves;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface KillerMoves {
    boolean trackKillerMove(Move killerMove, int currentPly);

    boolean isKiller(Move move, int currentPly);

    void reset();
}
