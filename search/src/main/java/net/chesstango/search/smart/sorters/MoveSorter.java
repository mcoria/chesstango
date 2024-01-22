package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface MoveSorter {
    List<Move> getSortedMoves();
}
