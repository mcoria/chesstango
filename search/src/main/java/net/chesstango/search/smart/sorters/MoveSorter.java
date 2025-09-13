package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface MoveSorter extends Acceptor {
    Iterable<Move> getOrderedMoves(final int currentPly);
}
