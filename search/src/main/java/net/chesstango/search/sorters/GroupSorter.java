package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface GroupSorter {
    int sort(List<Move> moves, int from, int to);
}
