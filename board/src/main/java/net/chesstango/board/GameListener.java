package net.chesstango.board;

import net.chesstango.board.moves.Move;

/**
 * Interface for listening to game events in a chess game.
 * Implementations of this interface can be used to receive notifications
 * when moves are made or undone in the game.
 *
 * @see Move
 *
 * @author Mauricio Coria
 */
public interface GameListener {

    /**
     * Notifies that a move has been made.
     *
     * @param move the move that was made
     */
    void notifyDoMove(Move move);

    /**
     * Notifies that a move has been undone.
     *
     * @param move the move that was undone
     */
    void notifyUndoMove(Move move);
}
