package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface MoveSorterElement {
    void sort(List<Move> unsortedMoves, List<Move> sortedMoves);
}
