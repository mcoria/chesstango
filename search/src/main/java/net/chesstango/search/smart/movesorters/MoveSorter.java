package net.chesstango.search.smart.movesorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchActions;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface MoveSorter extends SearchActions {
    List<Move> getSortedMoves();
}
