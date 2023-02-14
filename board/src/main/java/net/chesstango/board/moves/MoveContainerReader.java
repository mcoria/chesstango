package net.chesstango.board.moves;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveContainerReader extends Iterable<Move> {
    int size();

    boolean isEmpty();

    boolean contains(Move move);
}
