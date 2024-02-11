package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface MoveSorter {
    Iterable<Move> getOrderedMoves();
}
