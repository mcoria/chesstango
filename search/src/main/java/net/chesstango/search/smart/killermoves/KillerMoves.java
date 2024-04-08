package net.chesstango.search.smart.killermoves;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface KillerMoves {
    boolean trackKillerMove(int currentPly, Move killerMove);

    boolean o1IsKiller(Move o1, int currentPly);
}
