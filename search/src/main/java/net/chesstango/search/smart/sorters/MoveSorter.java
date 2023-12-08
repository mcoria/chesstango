package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchCycleListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface MoveSorter extends SearchCycleListener {
    List<Move> getSortedMoves();
}
