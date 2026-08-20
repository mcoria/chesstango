package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface MoveSorter {
    Iterable<Move> getOrderedMoves(final int currentPly);
}
