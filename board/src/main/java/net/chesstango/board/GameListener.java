package net.chesstango.board;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface GameListener {
    void notifyDoMove(Move move);

    void notifyUndoMove(Move move);
}
