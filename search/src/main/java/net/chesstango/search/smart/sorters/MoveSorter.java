package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface MoveSorter extends SearchListener {
    List<Move> getSortedMoves();
}
